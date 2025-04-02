package com.mooncloak.vpn.app.shared.util

import java.math.RoundingMode
import java.text.NumberFormat

public actual operator fun PercentageFormatter.Companion.invoke(): PercentageFormatter =
    AndroidPercentageFormatter()

internal class AndroidPercentageFormatter internal constructor() : PercentageFormatter {

    override fun formatFraction(value: Double, decimalPlaces: Int): String {
        val formatter = NumberFormat.getPercentInstance().apply {
            minimumFractionDigits = decimalPlaces
            roundingMode = RoundingMode.HALF_UP
        }

        return formatter.format(value)
    }
}
