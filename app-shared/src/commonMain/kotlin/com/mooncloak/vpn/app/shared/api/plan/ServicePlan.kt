package com.mooncloak.vpn.app.shared.api.plan

import com.mooncloak.vpn.app.shared.api.money.Price
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlin.time.Duration

/**
 * A specific [Plan] that is for the mooncloak VPN service.
 *
 * @property [duration] The duration that this plan provides access for.
 *
 * @property [totalThroughput] The amount of bytes that are allowed for throughput with this plan.
 */
@Serializable
@SerialName(value = Plan.TYPE_VPN_SERVICE_PLAN)
public data class ServicePlan public constructor(
    @SerialName(value = "id") public override val id: String,
    @SerialName(value = "price") public override val price: Price,
    @SerialName(value = "crypto_estimate") public override val cryptoEstimate: Price? = null,
    @SerialName(value = "active") public override val active: Boolean = true,
    @SerialName(value = "created") public override val created: Instant,
    @SerialName(value = "updated") public override val updated: Instant? = null,
    @SerialName(value = "usage_type") public override val usageType: UsageType = UsageType.Licensed,
    @SerialName(value = "trial") public override val trial: TrialPeriod? = null,
    @SerialName(value = "subscription") public override val subscription: SubscriptionPeriod? = null,
    @SerialName(value = "live_mode") public override val liveMode: Boolean = false,
    @SerialName(value = "nickname") public override val nickname: String? = null,
    @SerialName(value = "title") public override val title: String,
    @SerialName(value = "description") public override val description: String? = null,
    @SerialName(value = "highlight") public override val highlight: String? = null,
    @SerialName(value = "self") public override val self: String? = null,
    @SerialName(value = "metadata") public override val metadata: JsonObject? = null,
    @SerialName(value = "tax_code") public override val taxCode: TaxCode? = null,
    @SerialName(value = "breakdown") public override val breakdown: PlanBreakdown? = null,
    @SerialName(value = "duration") public val duration: Duration,
    @SerialName(value = "throughput") public val totalThroughput: Long? = null,
    @SerialName(value = "rx_throughput") public val rxThroughput: Long? = null,
    @SerialName(value = "tx_throughput") public val txThroughput: Long? = null
) : Plan
