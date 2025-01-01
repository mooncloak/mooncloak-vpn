package com.mooncloak.vpn.app.shared.api.plan

import com.mooncloak.vpn.app.shared.api.billing.PaymentProvider
import com.mooncloak.vpn.app.shared.api.money.Price
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Represents a plan to the mooncloak VPN service.
 *
 * @property [id] An opaque, unique identifier value for this plan.
 *
 * @property [provider] The [PaymentProvider] that handles the payment when signing-up to the plans.
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
 * @property [usageType] Information about how the plan is calculated in respect to usage.
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
public sealed interface Plan : Product {

    public val id: String
    public val provider: PaymentProvider
    public val price: Price
    public val cryptoEstimate: Price?
    public val active: Boolean
    public val created: Instant
    public val updated: Instant?
    public val usageType: UsageType
    public val trial: TrialPeriod?
    public val subscription: SubscriptionPeriod?
    public val liveMode: Boolean
    public val nickname: String?
    public val title: String
    public val description: String?
    public val highlight: String?
    public val self: String?
    public val metadata: JsonObject?
    public val taxCode: TaxCode?
    public val breakdown: PlanBreakdown?

    public companion object {

        public const val TYPE_VPN_SERVICE_PLAN: String = "vpn_plan"
    }
}
