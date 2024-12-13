package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents activated service plan details. An activated service plan, is a purchased plan.
 *
 * @property [id] A unique identifier for this service plan.
 *
 * @property [planId] The identifier of the [Plan] associated with this [ServiceSubscription].
 *
 * @property [created] The [Instant] that this plan was first purchased.
 *
 * @property [boosted] The [Instant] that this plan was last boosted. "Boosting" is extending a plan.
 *
 * @property [expiration] The [Instant] when this plan expires.
 *
 * @property [boostCount] The amount of times this plan was boosted, or `null` if unknown.
 *
 * @property [byteCount] The amount of bytes allowed by this plan, or `null` if unknown.
 */
@Immutable
@Serializable
public data class ServiceSubscription public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "plan_id") public val planId: String,
    @SerialName(value = "created") public val created: Instant,
    @SerialName(value = "boosted") public val boosted: Instant? = null,
    @SerialName(value = "expiration") public val expiration: Instant,
    @SerialName(value = "boosts") public val boostCount: Int? = null,
    @SerialName(value = "bytes") public val byteCount: Long? = null
)
