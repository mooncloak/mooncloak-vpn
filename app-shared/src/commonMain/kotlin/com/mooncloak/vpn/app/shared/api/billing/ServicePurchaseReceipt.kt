package com.mooncloak.vpn.app.shared.api.billing

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.token.TransactionToken
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class ServicePurchaseReceipt public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "plan_id") public val planId: String,
    @SerialName(value = "invoice_id") public val invoiceId: String? = null,
    @SerialName(value = "created") public val created: Instant,
    @SerialName(value = "updated") public val updated: Instant,
    @SerialName(value = "purchased") public val purchased: Instant,
    @SerialName(value = "provider") public val provider: PaymentProvider,
    @SerialName(value = "subscription") public val subscription: Boolean = false,
    @SerialName(value = "client_secret") public val clientSecret: String? = null,
    @SerialName(value = "token") public val token: TransactionToken,
    @SerialName(value = "signature") public val signature: String? = null,
    @SerialName(value = "quantity") public val quantity: Int? = null
)
