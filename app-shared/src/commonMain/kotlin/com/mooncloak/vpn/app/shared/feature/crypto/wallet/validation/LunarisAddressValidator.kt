package com.mooncloak.vpn.app.shared.feature.crypto.wallet.validation

import com.mooncloak.vpn.util.shared.validation.ValidationException
import com.mooncloak.vpn.util.shared.validation.Validator

public sealed class LunarisAddressValidationException(
    message: String? = null,
    cause: Throwable? = null
) : ValidationException(message, cause) {

    public class InvalidFormat public constructor(
        message: String? = null,
        cause: Throwable? = null
    ) : LunarisAddressValidationException(
        message = message ?: "Invalid address format: must be a valid Ethereum-style address",
        cause = cause
    )

    public class InvalidLength public constructor(
        message: String? = null,
        cause: Throwable? = null
    ) : LunarisAddressValidationException(
        message = message ?: "Invalid address length: must be 42 characters",
        cause = cause
    )

    public class EmptyAddress public constructor(
        message: String? = null,
        cause: Throwable? = null
    ) : LunarisAddressValidationException(
        message = message ?: "Address cannot be empty",
        cause = cause
    )
}

public class LunarisAddressValidator public constructor() : Validator<String, String> {

    override fun validate(value: String): Result<String> {
        // Check if empty or blank
        if (value.isBlank()) {
            return Result.failure(LunarisAddressValidationException.EmptyAddress())
        }

        // Check length
        if (value.length != VALID_LENGTH) {
            return Result.failure(LunarisAddressValidationException.InvalidLength())
        }

        // Check format using regex (0x followed by 40 hex characters)
        if (!ADDRESS_REGEX.matches(value)) {
            return Result.failure(LunarisAddressValidationException.InvalidFormat())
        }

        return Result.success(value)
    }

    public companion object {

        private val ADDRESS_REGEX = "^0x[0-9a-fA-F]{40}$".toRegex()
        private const val VALID_LENGTH = 42
    }
}
