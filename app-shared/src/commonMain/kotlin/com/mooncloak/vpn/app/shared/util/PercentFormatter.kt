package com.mooncloak.vpn.app.shared.util

public interface PercentageFormatter {

    public fun format(value: Double, decimalPlaces: Int = 2): String =
        formatFraction(value = value / 100, decimalPlaces = decimalPlaces)

    public fun formatFraction(value: Double, decimalPlaces: Int = 2): String

    public companion object
}

public expect operator fun PercentageFormatter.Companion.invoke(): PercentageFormatter

public fun PercentageFormatter.format(value: Number, decimalPlaces: Int = 2): String =
    this.format(value = value.toDouble(), decimalPlaces = decimalPlaces)

public fun PercentageFormatter.formatFraction(value: Number, decimalPlaces: Int = 2): String =
    this.formatFraction(value = value, decimalPlaces = decimalPlaces)
