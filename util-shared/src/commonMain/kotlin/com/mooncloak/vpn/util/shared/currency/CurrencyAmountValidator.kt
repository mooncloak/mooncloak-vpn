package com.mooncloak.vpn.util.shared.currency

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.mooncloak.kodetools.locale.ExperimentalLocaleApi
import com.mooncloak.kodetools.locale.Locale
import com.mooncloak.vpn.util.shared.locale.decimalSeparator
import com.mooncloak.vpn.util.shared.locale.groupingSeparator
import com.mooncloak.vpn.util.shared.validation.ValidatingConstructor

@OptIn(ExperimentalLocaleApi::class)
public fun Currency.Amount.Companion.lunarisValidator(locale: Locale = Locale.getDefault()): CurrencyAmountValidator =
    CurrencyAmountValidator(
        currency = Currency.Lunaris,
        unit = Currency.Unit.Minor,
        locale = locale
    )

@OptIn(ExperimentalLocaleApi::class)
public class CurrencyAmountValidator public constructor(
    public val currency: Currency,
    public val unit: Currency.Unit,
    private val locale: Locale,
    private val extraValidation: (value: String, number: BigDecimal) -> Boolean = { _, _ -> true }
) : ValidatingConstructor<String, Currency.Amount> {

    override fun validate(value: String): Result<Currency.Amount> {
        if (value.isBlank()) {
            return Result.failure(CurrencyAmountValidationException.InputBlank())
        }

        // We need to make sure that we format the locale-specific String the user entered into a String format that
        // the BigDecimal.parse function supports.
        val formatted = value.replace(locale.groupingSeparator, "") // Remove thousands separator
            .replace(locale.decimalSeparator, ".") // Normalize decimal separator to '.'

        val number = try {
            BigDecimal.parseString(formatted)
        } catch (e: Exception) {
            return Result.failure(CurrencyAmountValidationException.NotANumber(cause = e))
        }

        if (number <= BigDecimal.ZERO) {
            return Result.failure(CurrencyAmountValidationException.TooSmall())
        }

        // Check decimal precision
        val decimalPlaces = number.scale
        val maxDecimals = currency.defaultFractionDigits ?: 2
        if (decimalPlaces > maxDecimals) {
            return Result.failure(
                CurrencyAmountValidationException.IncorrectDecimals(message = "Too many decimal places (max $maxDecimals)")
            )
        }

        try {
            val isValid = extraValidation.invoke(formatted, number)

            if (!isValid) {
                return Result.failure(CurrencyAmountValidationException.Unknown())
            }
        } catch (e: Exception) {
            return if (e is CurrencyAmountValidationException) {
                Result.failure(e)
            } else {
                Result.failure(CurrencyAmountValidationException.Unknown(cause = e))
            }
        }

        return Result.success(
            Currency.Amount(
                currency = currency,
                unit = unit,
                value = number
            )
        )
    }
}
