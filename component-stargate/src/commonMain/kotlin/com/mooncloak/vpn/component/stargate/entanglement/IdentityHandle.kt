package com.mooncloak.vpn.component.stargate.entanglement

import com.mooncloak.vpn.util.shared.validation.ValidatingConstructor
import com.mooncloak.vpn.util.shared.validation.ValidationException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = IdentityHandleSerializer::class)
public class IdentityHandle private constructor(
    public val value: String,
    public val format: IdentityHandleFormat,
    public val localPart: String,
    public val remotePart: String? = null
) {

    public companion object : ValidatingConstructor<String, IdentityHandle> {

        override fun validate(value: String): Result<IdentityHandle> {
            val formatted = value.trim().removePrefix("@").lowercase()

            when {
                formatted.startsWith("did:") -> return validateDid(value = formatted)

                formatted.endsWith(":did") || formatted.endsWith(":web") -> return validateDidAlias(value = formatted)

                else -> {
                    var index = formatted.indexOf('@')
                    if (index >= 0) {
                        return validateActivityPub(value = formatted, separatorIndex = index)
                    }

                    index = formatted.indexOf(':')
                    if (index >= 0) {
                        return validateMatrix(value = formatted, separatorIndex = index)
                    }

                    index = formatted.lastIndexOf('.')
                    if (index >= 0) {
                        return validateAt(value = formatted, lastSeparatorIndex = index)
                    }

                    return validateLocal(value = formatted)
                }
            }
        }

        private fun validateDid(value: String): Result<IdentityHandle> {
            val result = DID.validate(value = value)

            return if (result.isSuccess) {
                Result.success(
                    IdentityHandle(
                        value = value,
                        format = IdentityHandleFormat.DID,
                        localPart = value,
                        remotePart = null
                    )
                )
            } else {
                Result.failure(
                    IdentityHandleValidationException(
                        message = "Invalid DID.",
                        cause = result.exceptionOrNull()
                    )
                )
            }
        }

        private fun validateDidAlias(value: String): Result<IdentityHandle> = Result.success(
            IdentityHandle(
                value = value,
                format = IdentityHandleFormat.ALIAS,
                localPart = value,
                remotePart = null
            )
        )

        private fun validateActivityPub(value: String, separatorIndex: Int): Result<IdentityHandle> =
            if (separatorIndex + 1 < value.length) {
                Result.success(
                    IdentityHandle(
                        value = value,
                        format = IdentityHandleFormat.ACTIVITY_PUB,
                        localPart = value.substring(startIndex = 0, endIndex = separatorIndex),
                        remotePart = value.substring(startIndex = separatorIndex + 1)
                    )
                )
            } else {
                validateLocal(value = value)
            }

        private fun validateMatrix(value: String, separatorIndex: Int): Result<IdentityHandle> =
            if (separatorIndex + 1 < value.length) {
                Result.success(
                    IdentityHandle(
                        value = value,
                        format = IdentityHandleFormat.MATRIX,
                        localPart = value.substring(startIndex = 0, endIndex = separatorIndex),
                        remotePart = value.substring(startIndex = separatorIndex + 1)
                    )
                )
            } else {
                validateLocal(value = value)
            }

        private fun validateAt(value: String, lastSeparatorIndex: Int): Result<IdentityHandle> {
            if (lastSeparatorIndex - 1 <= 0) {
                return Result.failure(
                    IdentityHandleValidationException(message = "Missing required local part.")
                )
            }

            if (lastSeparatorIndex + 1 >= value.length) {
                return validateLocal(value = value)
            }

            val nextIndex = value.substring(startIndex = 0, endIndex = lastSeparatorIndex).lastIndexOf('.')

            val localPart = if (nextIndex >= 0) {
                if (nextIndex - 1 <= 0) {
                    return Result.failure(
                        IdentityHandleValidationException(message = "Missing required local part.")
                    )
                }

                value.substring(startIndex = 0, endIndex = nextIndex)
            } else {


                value.substring(startIndex = 0, endIndex = lastSeparatorIndex)
            }

            if (localPart == ".") {
                return Result.failure(
                    IdentityHandleValidationException(message = "Invalid local part '$localPart'.")
                )
            }

            // TODO: Further validation.

            return Result.success(
                IdentityHandle(
                    value = value,
                    format = IdentityHandleFormat.AT,
                    localPart = localPart,
                    remotePart = value.substring(startIndex = lastSeparatorIndex + 1)
                )
            )
        }

        private fun validateLocal(value: String): Result<IdentityHandle> =
            Result.success(
                IdentityHandle(
                    value = value,
                    format = IdentityHandleFormat.LOCAL,
                    localPart = value,
                    remotePart = null
                )
            )
    }
}

/**
 * Serializes the [IdentityHandle] as a [String] value.
 *
 * > [!Warning]
 * > This will validate the value on deserialization.
 */
public object IdentityHandleSerializer : KSerializer<IdentityHandle> {

    override val descriptor: SerialDescriptor = String.serializer().descriptor

    override fun serialize(encoder: Encoder, value: IdentityHandle) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): IdentityHandle =
        IdentityHandle.from(value = decoder.decodeString())
}

public open class IdentityHandleValidationException public constructor(
    message: String? = null,
    cause: Throwable? = null
) : ValidationException(message, cause)
