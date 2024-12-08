package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class Price public constructor(
    @SerialName(value = "currency") public val currency: Currency,
    @SerialName(value = "amount") public val amount: Long,
    @SerialName(value = "formatted") public val formatted: String? = null
)
