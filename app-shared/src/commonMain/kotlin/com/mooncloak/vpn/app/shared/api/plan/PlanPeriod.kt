package com.mooncloak.vpn.app.shared.api.plan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

/**
 * Represents how long a plan period lasts.
 *
 * @property [interval] The duration of a single plan period.
 *
 * @property [amount] The amount of plan periods included in this plan.
 */
@Serializable
public data class PlanPeriod public constructor(
    @SerialName(value = "interval") public val interval: Duration,
    @SerialName(value = "amount") public val amount: Int
)

/**
 * The total [Duration] of this [PlanPeriod].
 */
public val PlanPeriod.duration: Duration
    inline get() = interval * amount
