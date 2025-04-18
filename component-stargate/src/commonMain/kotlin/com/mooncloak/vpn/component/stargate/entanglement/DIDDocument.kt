package com.mooncloak.vpn.component.stargate.entanglement

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * Represents a DID Document.
 *
 * > [!Note]
 * > This is an interface and not a serializable data class because the DID Document can be in different formats
 * > (JSON, CBOR, YAML, etc.).
 */
@Serializable(with = DIDDocumentSerializer::class)
public interface DIDDocument {

    public val id: String

    public val alsoKnownAs: List<String>

    public val verificationMethod: List<VerificationMethod>

    public val keyAgreement: List<VerificationMethod>

    public val service: List<Service>

    public companion object
}

/**
 * Represents a verification method (e.g., public key).
 */
@Serializable
public data class VerificationMethod public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "type") public val type: String,
    @SerialName(value = "controller") public val controller: String,
    @SerialName(value = "publicKeyBase58") public val publicKeyBase58: String? = null,
    @SerialName(value = "publicKeyJwk") public val publicKeyJwk: JsonObject? = null, // TODO: Is this a JSON object or a String? How can we represent this in non-JSON?
    @SerialName(value = "publicKeyMultibase") public val publicKeyMultibase: String? = null
)

/**
 * Represents a service endpoint.
 */
@Serializable
public data class Service public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "type") public val type: String,
    @SerialName(value = "serviceEndpoint") public val serviceEndpoint: String // Simplified; extend to JsonElement if needed
)

@Serializable
internal data class JsonDIDDocument internal constructor(
    @SerialName(value = "id") override val id: String,
    @SerialName(value = "alsoKnownAs") override val alsoKnownAs: List<String> = emptyList(),
    @SerialName(value = "verificationMethod") override val verificationMethod: List<VerificationMethod> = emptyList(),
    @SerialName(value = "keyAgreement") override val keyAgreement: List<VerificationMethod> = emptyList(),
    @SerialName(value = "service") override val service: List<Service> = emptyList()
) : DIDDocument

// TODO: Support other formats like CBOR
internal object DIDDocumentSerializer : KSerializer<DIDDocument> {

    override val descriptor: SerialDescriptor = JsonDIDDocument.serializer().descriptor

    override fun serialize(encoder: Encoder, value: DIDDocument) {
        val delegate = if (value is JsonDIDDocument) {
            value
        } else {
            JsonDIDDocument(
                id = value.id,
                alsoKnownAs = value.alsoKnownAs,
                verificationMethod = value.verificationMethod,
                keyAgreement = value.keyAgreement,
                service = value.service
            )
        }

        encoder.encodeSerializableValue(
            serializer = JsonDIDDocument.serializer(),
            value = delegate
        )
    }

    override fun deserialize(decoder: Decoder): DIDDocument =
        decoder.decodeSerializableValue(deserializer = JsonDIDDocument.serializer())
}
