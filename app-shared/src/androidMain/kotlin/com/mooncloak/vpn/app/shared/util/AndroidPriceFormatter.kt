package com.mooncloak.vpn.app.shared.util

import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.os.Build
import com.mooncloak.vpn.api.shared.plan.Price

public actual fun Price.format(): String? {
    this.formatted?.let { return it }

    val isUsd = currency.code == com.mooncloak.vpn.util.shared.currency.Currency.Code.USD

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val formatter = NumberFormat.getCurrencyInstance().apply {
            currency = Currency.getInstance(this@format.currency.code.value)
            maximumFractionDigits = if (isUsd && amount % 100L == 0L) {
                0
            } else {
                2
            }
        }

        val formattedAmount = when {
            // Special case handling for US dollar because that is our current market.
            isUsd -> this.amount * 0.01
            // TODO: Use fraction count to determine the minor unit fraction position: currency.defaultFractionDigits != null ->
            else -> this.amount
        }

        return formatter.format(formattedAmount)
    }

    return null
}
