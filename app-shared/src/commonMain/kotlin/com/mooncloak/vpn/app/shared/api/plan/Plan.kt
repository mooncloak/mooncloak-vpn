package com.mooncloak.vpn.app.shared.api.plan

import com.mooncloak.kodetools.textx.TextContent
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Represents a plan to the mooncloak VPN service.
 *
 * @property [id] An opaque, unique identifier value for this plan.
 *
 * @property [provider] The [BillingProvider] that handles the payment when signing-up to the plans.
 *
 * @property [price] The [Price] model for this plan.
 *
 * @property [conversion] The current conversion estimate of the [Price] to the requested [Currency].
 *
 * @property [active] Whether this plan is still active and can be purchased. This value can be toggled whether the
 * plan is currently in or out of the available window defined by the [starts] and [ends] properties.
 *
 * @property [live] Determines if this is a live plan or just a test plan. This value is `true` if it is a live
 * plan, or `false` if it is a test plan.
 *
 * @property [autoRenews] Whether this plan will automatically renew at the end of the [subscription] period for a
 * subscribed purchaser of this plan.
 *
 * @property [created] When this plan model was first created or `null` if unknown.
 *
 * @property [updated] When this plan model was last updated or `null` if unknown.
 *
 * @property [starts] When this plan is first available for use or `null` if it doesn't have a starting point.
 *
 * @property [ends] When this plan is no longer available for use or `null` if it doesn't have a ending point.
 *
 * @property [usageType] Information about how the plan is calculated in respect to usage.
 *
 * @property [trial] If this plan offers a free trial, provides details about the trial period and how often it lasts.
 *
 * @property [subscription] Provides details about the subscription period indicating how long it will last, or `null`
 * if it is a plan that doesn't have a subscription period. Note that whether this plan is renewed again for the
 * [subscription] period amount of time after the [subscription] has passed depends on the [autoRenews] property.
 *
 * @property [nickname] A brief description of the plan, hidden from customers.
 *
 * @property [title] The displayable title of this plan.
 *
 * @property [description] The brief displayable information about this plan.
 *
 * @property [details] A more detailed [description] about this plan.
 *
 * @property [highlight] A highlighted label about this plan (ex: "Most Popular", or "Best Value").
 *
 * @property [self] An optional URI [String] that points to a detailed website about this plan.
 *
 * @property [taxCode] The [TaxCode] that this plan belongs to.
 *
 * @property [metadata] An optional [JsonObject] that can contain arbitrary data about this plan.
 */
@Serializable
public data class Plan public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "provider") public val provider: BillingProvider,
    @SerialName(value = "price") public val price: Price,
    @SerialName(value = "conversion") public val conversion: Price? = null,
    @SerialName(value = "active") public val active: Boolean = false,
    @SerialName(value = "live") public val live: Boolean = false,
    @SerialName(value = "auto_renews") public val autoRenews: Boolean = false,
    @SerialName(value = "created") public val created: Instant? = null,
    @SerialName(value = "updated") public val updated: Instant? = null,
    @SerialName(value = "starts") public val starts: Instant? = null,
    @SerialName(value = "ends") public val ends: Instant? = null,
    @SerialName(value = "usage_type") public val usageType: UsageType = UsageType.Licensed,
    @SerialName(value = "trial") public val trial: PlanPeriod? = null,
    @SerialName(value = "subscription") public val subscription: PlanPeriod? = null,
    @SerialName(value = "nickname") public val nickname: String? = null,
    @SerialName(value = "title") public val title: String,
    @SerialName(value = "description") public val description: TextContent? = null,
    @SerialName(value = "details") public val details: TextContent? = null,
    @SerialName(value = "highlight") public val highlight: String? = null,
    @SerialName(value = "self") public val self: String? = null,
    @SerialName(value = "tax_code") public val taxCode: TaxCode? = null,
    @SerialName(value = "metadata") public val metadata: JsonObject? = null
) : Product
