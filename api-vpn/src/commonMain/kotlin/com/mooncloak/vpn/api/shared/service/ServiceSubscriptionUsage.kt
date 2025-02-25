package com.mooncloak.vpn.api.shared.service

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
 * @property [totalThroughputUsed] The total amount of throughput Bytes that were already used, or `null` if unknown.
 *
 * @property [totalThroughputRemaining] The total amount of throughput Bytes that are remaining, or `null` if unknown.
 *
 * @property [rxThroughputUsed] The total amount of incoming throughput Bytes that were already used, or `null` if
 * unknown.
 *
 * @property [rxThroughputRemaining] The total amount of incoming throughput Bytes that are remaining, or `null` if
 * unknown.
 *
 * @property [txThroughputUsed] The total amount of outgoing throughput Bytes that were already used, or `null` if
 * unknown.
 *
 * @property [txThroughputRemaining] The total amount of outgoing throughput Bytes that are remaining, or `null` if
 * unknown.
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
    @SerialName(value = "bytes_used") public val totalThroughputUsed: Long? = null,
    @SerialName(value = "bytes_remaining") public val totalThroughputRemaining: Long? = null,
    @SerialName(value = "rx_used") public val rxThroughputUsed: Long? = null,
    @SerialName(value = "rx_remaining") public val rxThroughputRemaining: Long? = null,
    @SerialName(value = "tx_used") public val txThroughputUsed: Long? = null,
    @SerialName(value = "tx_remaining") public val txThroughputRemaining: Long? = null,
    @SerialName(value = "duration_used") public val durationUsed: Duration? = null,
    @SerialName(value = "duration_remaining") public val durationRemaining: Duration? = null
)
