package com.mooncloak.vpn.app.shared.feature.crypto.wallet.validation

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.util.shared.validation.ValidationException
import com.mooncloak.vpn.util.shared.validation.Validator

public sealed class SecretRecoveryPhraseValidationException(
    message: String? = null,
    cause: Throwable? = null
) : ValidationException(message, cause) {

    public class InvalidLength public constructor(
        message: String? = null,
        cause: Throwable? = null
    ) : SecretRecoveryPhraseValidationException(
        message = message,
        cause = cause
    )

    public class IncorrectWordCount public constructor(
        message: String? = null,
        cause: Throwable? = null
    ) : SecretRecoveryPhraseValidationException(
        message = message,
        cause = cause
    )
}

public class SecretRecoveryPhraseValidator @Inject public constructor() : Validator<String, String> {

    override fun validate(value: String): Result<String> {
        if (value.isBlank()) {
            return Result.failure(SecretRecoveryPhraseValidationException.InvalidLength())
        }

        val words = value.trim().split("\\s+".toRegex())

        if (words.size != 12 && words.size != 24) {
            return Result.failure(SecretRecoveryPhraseValidationException.IncorrectWordCount(message = "Invalid seed phrase: must be 12 or 24 words"))
        }

        return Result.success(value)
    }
}
