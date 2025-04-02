package com.mooncloak.vpn.app.shared.util

import com.mooncloak.vpn.api.shared.plan.Price
import java.text.NumberFormat
import java.util.Currency

public actual fun Price.format(): String? {
    this.formatted?.let { return it }

    val isUsd = currency.code == com.mooncloak.vpn.api.shared.currency.Currency.Code.USD

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
