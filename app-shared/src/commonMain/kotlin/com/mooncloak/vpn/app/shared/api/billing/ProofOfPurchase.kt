package com.mooncloak.vpn.app.shared.api.billing

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.token.TransactionToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class ProofOfPurchase public constructor(
    @SerialName(value = "provider") public val paymentProvider: PaymentProvider,
    @SerialName(value = "id") public val id: String? = null,
    @SerialName(value = "client_secret") public val clientSecret: String? = null,
    @SerialName(value = "token") public val token: TransactionToken
)
