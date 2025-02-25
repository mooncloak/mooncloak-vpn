package com.mooncloak.vpn.api.shared.billing

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class GetPaymentStatusRequestBody public constructor(
    @SerialName(value = "payment_id") val paymentId: String,
    @SerialName(value = "secret") val secret: String? = null
)
