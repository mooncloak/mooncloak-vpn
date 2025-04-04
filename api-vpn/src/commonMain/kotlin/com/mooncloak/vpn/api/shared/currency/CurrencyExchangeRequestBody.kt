package com.mooncloak.vpn.api.shared.currency

import com.mooncloak.vpn.util.shared.currency.Currency
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CurrencyExchangeRequestBody public constructor(
    @SerialName(value = "amount") public val amount: Currency.Amount,
    @SerialName(value = "target") public val target: Currency
)
