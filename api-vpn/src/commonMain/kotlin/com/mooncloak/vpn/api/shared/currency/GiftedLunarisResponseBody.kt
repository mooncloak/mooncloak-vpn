package com.mooncloak.vpn.api.shared.currency

import com.mooncloak.vpn.util.shared.currency.Currency
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GiftedLunarisResponseBody public constructor(
    @SerialName(value = "timestamp") public val timestamp: Instant,
    @SerialName(value = "promo_code") public val promoCode: String? = null,
    @SerialName(value = "message") public val message: String? = null,
    @SerialName(value = "address") public val address: String,
    @SerialName(value = "amount") public val amount: Currency.Amount,
    @SerialName(value = "status") public val status: String? = null, // ex: pending, completed, etc.
    @SerialName(value = "expired") public val expired: Boolean = false // whether the promotion expired or not.
)
