package com.mooncloak.vpn.crypto.lunaris.model

import com.mooncloak.vpn.util.shared.currency.Currency
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GiftedCryptoTokenStats public constructor(
    @SerialName(value = "last_gifted") public val lastGifted: Instant,
    @SerialName(value = "total") public val total: Currency.Amount
)
