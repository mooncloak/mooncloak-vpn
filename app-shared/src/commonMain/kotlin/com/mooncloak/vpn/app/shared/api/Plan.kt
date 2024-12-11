package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Represents a plan to the mooncloak VPN service.
 *
 * @property [type] The type of this product model. This should always be "plan" for a [Plan] model.
 *
 * @property [id] An opaque, unique identifier value for this plan.
 *
 * @property [price] The [Price] model for this plan.
 *
 * @property [cryptoEstimate] The current conversion estimate of the [Price] to a cryptocurrency that is accepted as
 * payment.
 *
 * @property [active] Whether this plan is still active and can be purchased.
 *
 * @property [created] When this plan model was first created.
 *
 * @property [updated] When this plan model was last updated.
 *
 * @property [usageType] Information about how the plan is calculated in regards to usage.
 *
 * @property [trial] If this plan offers a free trial, provides details about the trial period and how often it lasts.
 *
 * @property [subscription] If this plan is a subscription, provides details about the subscription period indicating
 * how often it is billed. If this value is `null`, then the billing is done once at the time of purchase as there is
 * no subscription.
 *
 * @property [liveMode] Determines if this is a live plan or just a test plan. This value is `true` if it is a live
 * plan, or `false` if it is a test plan.
 *
 * @property [nickname] A brief description of the plan, hidden from customers.
 *
 * @property [title] The displayable title of this plan.
 *
 * @property [description] The displayable details about this plan.
 *
 * @property [highlight] A highlighted label about this plan (ex: "Most Popular", or "Best Value").
 *
 * @property [self] An optional URI [String] that points to a detailed website about this plan.
 *
 * @property [metadata] An optional [JsonObject] that can contain arbitrary data about this plan.
 *
 * @property [taxCode] The [TaxCode] that this plan belongs to.
 *
 * @property [breakdown] Detailed information about this plan typically displayed in a bulleted list.
 */
@Serializable
@Immutable
public data class Plan public constructor(
    @SerialName(value = "type") public val type: String = "plan", // Used for future improvements to the API, such as using sealed classes for different product types.
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "price") public val price: Price,
    @SerialName(value = "crypto_estimate") public val cryptoEstimate: Price? = null,
    @SerialName(value = "active") public val active: Boolean = true,
    @SerialName(value = "created") public val created: Instant,
    @SerialName(value = "updated") public val updated: Instant? = null,
    @SerialName(value = "usage_type") public val usageType: UsageType = UsageType.Licensed,
    @SerialName(value = "trial") public val trial: TrialPeriod? = null,
    @SerialName(value = "subscription") public val subscription: SubscriptionPeriod? = null,
    @SerialName(value = "live_mode") public val liveMode: Boolean = false,
    @SerialName(value = "nickname") public val nickname: String? = null,
    @SerialName(value = "title") public val title: String,
    @SerialName(value = "description") public val description: String? = null,
    @SerialName(value = "highlight") public val highlight: String? = null,
    @SerialName(value = "self") public val self: String? = null,
    @SerialName(value = "metadata") public val metadata: JsonObject? = null,
    @SerialName(value = "tax_code") public val taxCode: TaxCode? = null,
    @SerialName(value = "breakdown") public val breakdown: PlanBreakdown? = null
)
