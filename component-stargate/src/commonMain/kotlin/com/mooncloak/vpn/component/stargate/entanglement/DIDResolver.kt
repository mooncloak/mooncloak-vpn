package com.mooncloak.vpn.component.stargate.entanglement

import com.mooncloak.vpn.util.shared.validation.fromOrNull
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

public interface DIDResolver {

    public suspend fun resolve(handle: IdentityHandle): DID

    public companion object
}

public class ATProtocolDIDResolver public constructor(
    private val defaultDomainProvider: DomainProvider,
    private val dnsTxtRecordResolver: DnsTxtRecordResolver,
    private val httpClient: HttpClient,
    private val json: Json
) : DIDResolver {

    override suspend fun resolve(handle: IdentityHandle): DID = coroutineScope {
        when (handle.format) {
            IdentityHandleFormat.DID -> DID.from(value = handle.value)

            IdentityHandleFormat.ALIAS -> {
                val value = handle.value
                    .split(':')
                    .reversed()
                    .filter { it.isNotBlank() }
                    .joinToString(separator = ":")

                DID.from(value = if (value.startsWith("did:")) value else "did:$value")
            }

            else -> {
                val localPart = handle.localPart

                var remotePart = handle.remotePart
                if (remotePart.isNullOrBlank()) {
                    remotePart = defaultDomainProvider.get()
                }

                // Note that we construct the value from the different parts manually here because the handle.value can
                // contain different separators (ex: '@' for ActivityPub or ':' for Matrix). We are flexible for what
                // we accept for a user handle but we have to format it properly for resolution and we currently only
                // support the AT Protocol for DID resolution.
                val handleValue = "$localPart.$remotePart"

                // See the AT Protocol documentation for resolving the DID from the user handle:
                // https://atproto.com/specs/handle

                // From the documentation:
                //
                // > It is ok to attempt both resolution methods in parallel, and to use the first successful result
                // > available. If the two methods return conflicting results (aka, different DIDs), the DNS TXT result
                // > should be preferred, though it is also acceptable to record the result as ambiguous and try again
                // > later.
                val deferredDns = async {
                    dnsTxtRecordResolver.resolve("_atproto.$handleValue")
                        .firstOrNull { it.startsWith("did=") }
                        ?.substringAfter("did=")
                        ?.let { DID.fromOrNull(value = it) }
                }

                val deferredWellKnown = async {
                    val httpResponse = httpClient.get("https://$handleValue/.well-known/atproto-did")

                    if (!httpResponse.status.isSuccess()) {
                        null
                    } else {
                        DID.fromOrNull(value = httpResponse.bodyAsText())
                    }
                }

                // If we can't resolve the other values, try and guess the PDS server from the handle and attempt to
                // resolve it that way.
                val deferredPds = async {
                    val httpResponse =
                        httpClient.get("https://$remotePart/xrpc/com.atproto.identity.resolveHandle?handle=$handleValue")

                    if (!httpResponse.status.isSuccess()) {
                        null
                    } else {
                        try {
                            val jsonText = httpResponse.bodyAsText()
                            val did = json.decodeFromString(deserializer = PdsDid.serializer(), string = jsonText)

                            DID.fromOrNull(value = did.did)
                        } catch (_: Exception) {
                            null
                        }
                    }
                }

                val dnsDid = deferredDns.await()
                val wellKnownDid = deferredWellKnown.await()
                val pdsDid = deferredPds.await()

                dnsDid ?: wellKnownDid ?: pdsDid ?: error("Failed to resolve DID.")
            }
        }
    }

    public companion object
}

@Serializable
internal data class PdsDid internal constructor(
    @SerialName(value = "did") val did: String
)
