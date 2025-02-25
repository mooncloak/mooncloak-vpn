package com.mooncloak.vpn.api.shared.vpn

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class TunnelStats public constructor(
    @SerialName(value = "rx_throughput") public val rxThroughput: Long? = null,
    @SerialName(value = "tx_throughput") public val txThroughput: Long? = null,
    @SerialName(value = "total_rx") public val totalRx: Long? = null,
    @SerialName(value = "total_tx") public val totalTx: Long? = null
)
