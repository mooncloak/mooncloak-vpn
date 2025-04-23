package com.mooncloak.vpn.component.stargate.entanglement

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

public interface DIDDocumentResolver {

    public suspend fun resolve(did: DID): DIDDocument

    public companion object
}

public class DIDWebDocumentResolver public constructor(
    private val httpClient: HttpClient
) : DIDDocumentResolver {

    override suspend fun resolve(did: DID): DIDDocument {
        val identifier = did.id ?: error("DID id part was required but was missing.")

        // Convert identifier to URL (e.g., chris.keenan -> https://chris.keenan/.well-known/did.json)
        val domain = identifier.replace(":", "/") // Handle paths (e.g., example.com:users:chris)
        val wellKnownUrl = "https://$domain/.well-known/did.json"
        val fallbackUrl = "https://$domain/did.json"

        return try {
            val document: DIDDocument = httpClient.get(wellKnownUrl).body()

            document.validateDidDocument(did)

            document
        } catch (e: Exception) {
            // Try alternative path (e.g., https://chris.keenan/did.json)
            val document: DIDDocument = httpClient.get(fallbackUrl).body()

            document.validateDidDocument(did)

            document
        }
    }

    private fun DIDDocument.validateDidDocument(did: DID) {
        if (this.id != did.value) {
            error("DID document id '${this.id}' does not match expected DID '${did.value}'.")
        }
    }
}

public class DIDPlcDocumentResolver public constructor(
    private val httpClient: HttpClient
) : DIDDocumentResolver {

    override suspend fun resolve(did: DID): DIDDocument {
        // Query PLC directory (e.g., https://plc.directory/did:plc:abc123)
        val url = "https://plc.directory/${did.value}"

        val document: DIDDocument = httpClient.get(url).body()

        document.validateDidDocument(did)

        return document
    }

    private fun DIDDocument.validateDidDocument(did: DID) {
        if (this.id != did.value) {
            error("DID document id '${this.id}' does not match expected DID '${did.value}'.")
        }
    }
}

public class DefaultDIDDocumentResolver public constructor(
    httpClient: HttpClient
) : DIDDocumentResolver {

    private val didWebDocumentResolver = DIDWebDocumentResolver(httpClient = httpClient)
    private val didPlcDocumentResolver = DIDPlcDocumentResolver(httpClient = httpClient)

    override suspend fun resolve(did: DID): DIDDocument =
        when (val method = did.method) {
            "web" -> didWebDocumentResolver.resolve(did)
            "plc" -> didPlcDocumentResolver.resolve(did)
            // TODO: Possibly support other well known DID methods.
            else -> error("Unsupported DID method '$method'")
        }
}
