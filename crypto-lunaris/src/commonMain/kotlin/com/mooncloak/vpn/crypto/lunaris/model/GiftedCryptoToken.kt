package com.mooncloak.vpn.crypto.lunaris.model

import com.mooncloak.vpn.util.shared.currency.Currency
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GiftedCryptoToken public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "created") public val created: Instant,
    @SerialName(value = "updated") public val updated: Instant,
    @SerialName(value = "gifted") public val gifted: Instant,
    @SerialName(value = "promo_code") public val promoCode: String? = null,
    @SerialName(value = "message") public val message: String? = null,
    @SerialName(value = "address") public val address: String,
    @SerialName(value = "amount") public val amount: Currency.Amount
)
