package com.mooncloak.vpn.component.stargate.entanglement

import com.mooncloak.vpn.util.shared.validation.fromOrNull
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

public class DIDResolver public constructor(
    private val defaultDomainProvider: DomainProvider,
    private val dnsTxtRecordResolver: DnsTxtRecordResolver,
    private val httpClient: HttpClient
) {

    public suspend fun resolve(handle: IdentityHandle): DID =
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
                val value = "$localPart.$remotePart"

                // See the AT Protocol documentation for resolving the DID from the user handle:
                // https://atproto.com/specs/handle

                val record = dnsTxtRecordResolver.resolve("_atproto.$value")
                    .firstOrNull { it.startsWith("did=") }
                    ?.substringAfter("did=")
                    ?.let { DID.fromOrNull(value = it) }

                if (record != null) {
                    record
                } else {
                    val httpResponse = httpClient.get("https://$value/.well-known/atproto-did")

                    if (!httpResponse.status.isSuccess()) {
                        error("Failed to resolve DID.")
                    }

                    DID.from(value = httpResponse.bodyAsText())
                }
            }
        }

    public companion object
}
