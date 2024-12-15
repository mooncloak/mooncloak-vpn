package com.mooncloak.vpn.app.shared.api.service

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

/**
 * Represents usage data about a [ServiceSubscription].
 *
 * @property [subscriptionId] The identifier of the [ServiceSubscription] that this usage represents.
 *
 * @property [timestamp] The [Instant] that this usage data was retrieved.
 *
 * @property [active] Whether the corresponding [ServiceSubscription] is active at the [timestamp].
 *
 * @property [bytesUsed] The amount of Bytes that were already used, or `null` if unknown.
 *
 * @property [bytesRemaining] The amount of Bytes that are remaining, or `null` if unknown.
 *
 * @property [durationUsed] The [Duration] of time that has elapsed since the start of the [ServiceSubscription], or
 * `null` if unknown.
 *
 * @property [durationRemaining] The [Duration] of time that is remaining for the [ServiceSubscription], relative to
 * the [timestamp], or `null` if unknown.
 */
@Immutable
@Serializable
public data class ServiceSubscriptionUsage public constructor(
    @SerialName(value = "subscription_id") public val subscriptionId: String,
    @SerialName(value = "timestamp") public val timestamp: Instant,
    @SerialName(value = "active") public val active: Boolean = false,
    @SerialName(value = "bytes_used") public val bytesUsed: Long? = null,
    @SerialName(value = "bytes_remaining") public val bytesRemaining: Long? = null,
    @SerialName(value = "duration_used") public val durationUsed: Duration? = null,
    @SerialName(value = "duration_remaining") public val durationRemaining: Duration? = null
)
