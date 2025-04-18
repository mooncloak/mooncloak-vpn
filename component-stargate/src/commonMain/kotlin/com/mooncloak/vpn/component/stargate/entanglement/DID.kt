package com.mooncloak.vpn.component.stargate.entanglement

import com.mooncloak.vpn.util.shared.validation.ValidatingConstructor
import com.mooncloak.vpn.util.shared.validation.ValidationException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline

/**
 * Represents a DID (decentralized identifier). A DID is a valid URI with a custom scheme used for decentralized
 * identification. This is a String Value whose [value] takes the DID URI form: "did:method:id".
 */
@JvmInline
@Serializable(with = DIDSerializer::class)
public value class DID internal constructor(
    public val value: String
) {

    /**
     * Retrieves the DID method which is the part after the "did:" scheme in a valid DID URI value. The method is what
     * defines how a DID can be used (how DIDs are created, updated, retrieved, etc.). Technically, anyone can create a
     * method and define their own rules for a DID, but there is a central registry for common DID methods.
     */
    public val method: String
        get() {
            check(value.startsWith("did:")) { "Invalid DID state. A valid DID must start with 'did:' but value was '$value'." }

            var methodEnd = value.indexOf(":", startIndex = 4)
            if (methodEnd == -1) {
                methodEnd = value.length
            }

            return value.substring(startIndex = 4, endIndex = methodEnd)
        }

    /**
     * Retrieves the DID identifier value which is the last part of a valid DID URI value, and comes after the method.
     * The structure of the identifier value is up to the [method]'s rules, and a method may allow no identifier
     * values, which is why this value is nullable.
     */
    public val id: String?
        get() {
            check(value.startsWith("did:")) { "Invalid DID state. A valid DID must start with 'did:' but value was '$value'." }

            val methodEnd = value.indexOf(":", startIndex = 4)

            if (methodEnd == -1 || methodEnd + 1 > value.length) {
                return null
            }

            return value.substring(startIndex = methodEnd + 1)
        }

    public companion object : ValidatingConstructor<String, DID> {

        @Suppress("MemberVisibilityCanBePrivate")
        public const val METHOD_NAME_PATTERN: String = "[a-z][a-z0-9-]*"

        @Suppress("MemberVisibilityCanBePrivate")
        public val MethodNameRegex: Regex = Regex(METHOD_NAME_PATTERN)

        /**
         * Validates that the provided [String] DID [value] is a properly formatted DID.
         *
         * @param [value] The [String] DID to be validated.
         *
         * @return A [Result] with an [DID] value if validation was successful, otherwise a [Result.Failure]
         * containing an [DIDValidationException].
         */
        public override fun validate(value: String): Result<DID> {
            val formattedValue = value.trim()

            if (formattedValue.isEmpty()) {
                return Result.failure(DIDValidationException("A DID URI cannot be empty."))
            }

            if (!formattedValue.startsWith("did:")) {
                return Result.failure(DIDValidationException("A valid DID URI must start with the 'did' scheme."))
            }

            // Verify that there is at least one character after the did scheme that represents the "method" of the
            // DID. The identifier part of a DID is method specific and some methods might allow no identifiers, so we
            // don't check that.
            return if (formattedValue.length >= 5) {
                var methodEnd = formattedValue.indexOf(":", startIndex = 4)
                if (methodEnd == -1) {
                    methodEnd = formattedValue.length
                }

                val method = formattedValue.substring(startIndex = 4, endIndex = methodEnd)

                if (method.matches(MethodNameRegex)) {
                    Result.success(DID(value = formattedValue))
                } else {
                    Result.failure(DIDValidationException("Invalid method format '$method'."))
                }
            } else {
                Result.failure(DIDValidationException("A valid DID URI must contain a 'method' after the 'did' scheme."))
            }
        }
    }
}

/**
 * Serializes the [DID] as a [String] value.
 *
 * > [!Warning]
 * > This will validate the value on deserialization.
 */
public object DIDSerializer : KSerializer<DID> {

    override val descriptor: SerialDescriptor = String.serializer().descriptor

    override fun serialize(encoder: Encoder, value: DID) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): DID =
        DID.from(value = decoder.decodeString())
}

public open class DIDValidationException public constructor(
    message: String? = null,
    cause: Throwable? = null
) : ValidationException(message, cause)
