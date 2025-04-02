package com.mooncloak.vpn.app.shared.util

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterPercentStyle

public actual operator fun PercentageFormatter.Companion.invoke(): PercentageFormatter =
    IosPercentageFormatter()

internal class IosPercentageFormatter internal constructor() : PercentageFormatter {

    override fun formatFraction(value: Double, decimalPlaces: Int): String {
        val formatter = NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterPercentStyle
            minimumFractionDigits = decimalPlaces.toULong()
            maximumFractionDigits = decimalPlaces.toULong()
        }

        return formatter.stringFromNumber(NSNumber(value / 100.0)) ?: "${value}%"
    }
}
