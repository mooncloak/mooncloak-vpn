package com.mooncloak.vpn.api.shared.currency

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.decimal.DecimalMode
import com.ionspin.kotlin.bignum.decimal.RoundingMode

public operator fun Currency.Amount.Companion.invoke(
    currency: Currency,
    unit: Currency.Unit,
    value: Number
): Currency.Amount = DefaultCurrencyAmount(
    currency = currency,
    unit = unit,
    value = when (value) {
        is Byte -> BigDecimal.fromByte(value)
        is Short -> BigDecimal.fromShort(value)
        is Int -> BigDecimal.fromInt(value)
        is Long -> BigDecimal.fromLong(value)
        is Float -> BigDecimal.fromFloat(value)
        else -> BigDecimal.fromDouble(value.toDouble())
    }
)

internal class DefaultCurrencyAmount internal constructor(
    override val currency: Currency,
    override val unit: Currency.Unit,
    private val value: BigDecimal
) : Currency.Amount {

    private val fractionDigits: Int = currency.defaultFractionDigits ?: 2

    override fun toMinorUnits(): MinorUnits = when (unit) {
        Currency.Unit.Minor -> value.longValue()
        Currency.Unit.Major -> value.multiply(
            other = BigDecimal.TEN.pow(fractionDigits),
            decimalMode = DecimalMode(
                decimalPrecision = fractionDigits.toLong(),
                roundingMode = RoundingMode.FLOOR
            )
        ).longValue()
    }

    override fun toMajorUnits(): MajorUnits = when (unit) {
        Currency.Unit.Minor -> value.divide(
            other = BigDecimal.TEN.pow(fractionDigits),
            decimalMode = DecimalMode(
                decimalPrecision = fractionDigits.toLong(),
                roundingMode = RoundingMode.AWAY_FROM_ZERO
            )
        )

        Currency.Unit.Major -> value
    }
}
