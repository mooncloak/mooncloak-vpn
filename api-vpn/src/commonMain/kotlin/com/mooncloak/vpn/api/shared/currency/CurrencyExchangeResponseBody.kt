package com.mooncloak.vpn.api.shared.currency

import com.mooncloak.vpn.util.shared.currency.Currency
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CurrencyExchangeResponseBody public constructor(
    @SerialName(value = "timestamp") public val timestamp: Instant,
    @SerialName(value = "base") public val base: Currency.Amount,
    @SerialName(value = "target") public val target: Currency.Amount
)
