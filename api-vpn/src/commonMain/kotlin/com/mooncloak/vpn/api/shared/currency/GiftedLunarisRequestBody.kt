package com.mooncloak.vpn.api.shared.currency

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GiftedLunarisRequestBody public constructor(
    @SerialName(value = "address") public val address: String,
    @SerialName(value = "promo_code") public val promoCode: String? = null
)
