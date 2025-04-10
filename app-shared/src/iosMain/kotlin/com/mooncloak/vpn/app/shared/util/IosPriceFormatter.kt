package com.mooncloak.vpn.app.shared.util

import com.mooncloak.vpn.api.shared.plan.Price
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import kotlin.math.pow

public actual fun Price.format(): String? {
    // Create a NumberFormatter
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterCurrencyStyle
        setCurrencyCode(currency.code.value)
    }

    // Convert amount from minor units to major units
    val fractionDigits = currency.defaultFractionDigits ?: 2
    if (fractionDigits < 0) return null

    val divisor = 10.0.pow(fractionDigits.toDouble())
    val decimalAmount = amount.toDouble() / divisor

    // Format the number
    return formatter.stringFromNumber(NSNumber(decimalAmount))
}
