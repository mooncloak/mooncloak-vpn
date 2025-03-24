package com.mooncloak.vpn.api.shared.speedtest

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the result of performing a speed test of the network.
 *
 * @property [timestamp] The [Instant] that the speed test was performed.
 *
 * @property [serverId] The unique identifier of the server that was reached in order to perform the speed test.
 *
 * @property [ping] The RTT (Round Trip Time), in milliseconds, for the ping.
 *
 * @property [download] The amount of bytes per second for the download speed.
 *
 * @property [upload] The amount of bytes per second for the upload speed.
 */
@Serializable
public data class SpeedTestResult public constructor(
    @SerialName(value = "timestamp") public val timestamp: Instant,
    @SerialName(value = "server_id") public val serverId: String? = null,
    @SerialName(value = "ping") public val ping: Double,
    @SerialName(value = "download") public val download: Long,
    @SerialName(value = "upload") public val upload: Long
)
