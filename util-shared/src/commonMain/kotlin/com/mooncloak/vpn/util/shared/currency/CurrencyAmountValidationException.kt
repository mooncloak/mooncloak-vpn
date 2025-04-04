package com.mooncloak.vpn.util.shared.currency

import com.mooncloak.vpn.util.shared.validation.ValidationException

public sealed class CurrencyAmountValidationException protected constructor(
    message: String? = null,
    cause: Throwable? = null
) : ValidationException(message, cause) {

    public class InputBlank public constructor(
        message: String? = null,
        cause: Throwable? = null
    ) : CurrencyAmountValidationException(
        message = message,
        cause = cause
    )

    public class NotANumber public constructor(
        message: String? = null,
        cause: Throwable? = null
    ) : CurrencyAmountValidationException(
        message = message,
        cause = cause
    )

    public class TooSmall public constructor(
        message: String? = null,
        cause: Throwable? = null
    ) : CurrencyAmountValidationException(
        message = message,
        cause = cause
    )

    public class IncorrectDecimals public constructor(
        message: String? = null,
        cause: Throwable? = null
    ) : CurrencyAmountValidationException(
        message = message,
        cause = cause
    )

    public class Unknown public constructor(
        message: String? = null,
        cause: Throwable? = null
    ) : CurrencyAmountValidationException(
        message = message,
        cause = cause
    )
}
