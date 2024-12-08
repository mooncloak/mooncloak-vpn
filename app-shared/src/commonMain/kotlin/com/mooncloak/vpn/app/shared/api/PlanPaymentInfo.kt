package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents information about how to pay for a plan.
 *
 * @property [type] The type of this plan info model.
 *
 * @property [id] An opaque, unique identifier value for this plan payment info, used to associate this
 * information to a particular transaction when purchasing.
 *
 * @property [token] An opaque unique token [String] value generated for the transaction associated with this payment
 * info instance. If the user purchases the plan, via the properties on this model, then the user can obtain the status
 * of the purchase using this token for authorization.
 *
 * @property [created] The [Instant] that this model was first created.
 *
 * @property [uri] The URI [String] to open a wallet to pay for the plan. For instance, this would be a BIP 21 URI for
 * Bitcoin.
 *
 * @property [self] A URI [String] that points to a detailed website about this plan payment info.
 *
 * @property [amount] The [Price] model representing the amount required to complete this transaction.
 *
 * @property [address] The wallet address for a crypto payment.
 *
 * @property [label] A label to identify the recipient or purpose of the payment.
 *
 * @property [message] A message to display to the user, providing additional information about the payment.
 */
@Immutable
@Serializable
public data class PlanPaymentInfo public constructor(
    @SerialName(value = "type") public val type: String = "bitcoin", // Used for future improvements to the API, such as using sealed classes for different crypto types.
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "token") public val token: String,
    @SerialName(value = "created") public val created: Instant,
    @SerialName(value = "uri") public val uri: String? = null,
    @SerialName(value = "self") public val self: String? = null,
    @SerialName(value = "amount") public val amount: Price,
    @SerialName(value = "address") public val address: String? = null,
    @SerialName(value = "label") public val label: String? = null,
    @SerialName(value = "message") public val message: String? = null
)
