package com.mooncloak.vpn.app.shared.api.plan

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
public data class ServicePlanDetails public constructor(
    @SerialName(value = "duration") public val duration: Duration,
    @SerialName(value = "throughput") public val totalThroughput: Long? = null,
    @SerialName(value = "rx_throughput") public val rxThroughput: Long? = null,
    @SerialName(value = "tx_throughput") public val txThroughput: Long? = null
)
