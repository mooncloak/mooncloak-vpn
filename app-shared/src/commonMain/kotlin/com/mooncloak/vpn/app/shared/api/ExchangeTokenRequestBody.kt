package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
internal data class ExchangeTokenRequestBody internal constructor(
    @SerialName(value = "payment_id") val paymentId: String,
    @SerialName(value = "secret") val secret: String? = null
)
