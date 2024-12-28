package com.mooncloak.vpn.app.shared.api.server

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
public sealed interface ServerConnection {

    public val status: ServerConnectionStatus

    @Serializable
    public data class Connecting public constructor(
        @SerialName(value = "server") public val server: Server,
        @SerialName(value = "timestamp") public val timestamp: Instant
    ) : ServerConnection {

        @Transient
        override val status: ServerConnectionStatus = ServerConnectionStatus.Connecting
    }

    @Serializable
    public data class Connected public constructor(
        @SerialName(value = "session_id") public val sessionId: String,
        @SerialName(value = "server") public val server: Server,
        @SerialName(value = "timestamp") public val timestamp: Instant,
        @SerialName(value = "rx_throughput") public val rxThroughput: Long? = null,
        @SerialName(value = "tx_throughput") public val txThroughput: Long? = null
    ) : ServerConnection {

        @Transient
        override val status: ServerConnectionStatus = ServerConnectionStatus.Connected
    }

    @Serializable
    public data object Disconnected : ServerConnection {

        @Transient
        override val status: ServerConnectionStatus = ServerConnectionStatus.Disconnected
    }

    public companion object
}
