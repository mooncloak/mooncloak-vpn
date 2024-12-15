package com.mooncloak.vpn.app.shared.api.billing

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the request body for obtaining a [BitcoinPlanInvoice] model.
 *
 * @property [planId] The identifier of the [Plan] to request payment info about.
 *
 * @property [secret] A secret [String] value used to further check the status for the returned payment info
 * transaction. If this is provided, it MUST be provided for subsequent requests to obtain the status about the state
 * of a payment transaction for the returned payment info.
 */
@Immutable
@Serializable
internal data class GetPaymentInvoiceRequestBody internal constructor(
    @SerialName(value = "plan_id") val planId: String,
    @SerialName(value = "secret") val secret: String? = null
)
