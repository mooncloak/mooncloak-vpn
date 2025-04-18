package com.mooncloak.vpn.component.stargate.entanglement

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

public class DIDDocumentResolver public constructor(
    private val httpClient: HttpClient
) {

    public suspend fun resolve(did: DID): JsonObject =
        when (val method = did.method) {
            "web" -> resolveDidWeb(did)
            "plc" -> resolveDidPlc(did)
            // TODO: Possibly support other well known DID methods.
            else -> error("Unsupported DID method '$method'")
        }

    private suspend fun resolveDidWeb(did: DID): JsonObject {
        val identifier = did.id ?: error("DID id part was required but was missing.")

        // Convert identifier to URL (e.g., chris.keenan -> https://chris.keenan/.well-known/did.json)
        val domain = identifier.replace(":", "/") // Handle paths (e.g., example.com:users:chris)
        val wellKnownUrl = "https://$domain/.well-known/did.json"
        val fallbackUrl = "https://$domain/did.json"

        return try {
            val document: JsonObject = httpClient.get(wellKnownUrl).body()

            document.validateDidDocument(did)

            document
        } catch (e: Exception) {
            // Try alternative path (e.g., https://chris.keenan/did.json)
            val document: JsonObject = httpClient.get(fallbackUrl).body()

            document.validateDidDocument(did)

            document
        }
    }

    private suspend fun resolveDidPlc(did: DID): JsonObject {
        // Query PLC directory (e.g., https://plc.directory/did:plc:abc123)
        val url = "https://plc.directory/${did.value}"

        val document: JsonObject = httpClient.get(url).body()

        document.validateDidDocument(did)

        return document
    }

    private fun JsonObject.validateDidDocument(did: DID) {
        if (this["id"]?.jsonPrimitive?.contentOrNull != did.value) {
            error("DID does not match.")
        }
    }
}
