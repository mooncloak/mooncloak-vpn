package com.mooncloak.vpn.app.shared.api.billing

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.plan.BillingProvider
import com.mooncloak.vpn.app.shared.api.token.TransactionToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class ProofOfPurchase public constructor(
    @SerialName(value = "provider") public val paymentProvider: BillingProvider,
    @SerialName(value = "token") public val token: TransactionToken,
    @SerialName(value = "id") public val id: String? = null, // Order id is different from product ids in Google Play billing
    @SerialName(value = "products") public val productIds: List<String>? = null,
    @SerialName(value = "client_secret") public val clientSecret: String? = null
)
