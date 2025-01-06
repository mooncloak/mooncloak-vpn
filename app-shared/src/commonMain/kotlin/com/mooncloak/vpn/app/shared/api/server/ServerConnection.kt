package com.mooncloak.vpn.app.shared.api.server

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Serializable
public sealed interface ServerConnection {

    public val status: ServerConnectionStatus

    @Serializable
    @SerialName(value = "connecting")
    public data class Connecting public constructor(
        @SerialName(value = "server") public val server: Server,
        @SerialName(value = "timestamp") public val timestamp: Instant
    ) : ServerConnection {

        @Transient
        override val status: ServerConnectionStatus = ServerConnectionStatus.Connecting
    }

    @Serializable
    @SerialName(value = "connected")
    public data class Connected public constructor(
        @SerialName(value = "session_id") public val sessionId: String,
        @SerialName(value = "server") public val server: Server,
        @SerialName(value = "timestamp") public val timestamp: Instant,
        @SerialName(value = "rx_throughput") public val rxThroughput: Long? = null,
        @SerialName(value = "tx_throughput") public val txThroughput: Long? = null,
        @SerialName(value = "total_rx") public val totalRx: Long? = null,
        @SerialName(value = "total_tx") public val totalTx: Long? = null
    ) : ServerConnection {

        @Transient
        override val status: ServerConnectionStatus = ServerConnectionStatus.Connected
    }

    @Serializable
    @SerialName(value = "disconnecting")
    public data class Disconnecting public constructor(
        @SerialName(value = "server") public val server: Server? = null,
        @SerialName(value = "timestamp") public val timestamp: Instant
    ) : ServerConnection {

        @Transient
        override val status: ServerConnectionStatus = ServerConnectionStatus.Disconnecting
    }

    @Serializable
    @SerialName(value = "disconnected")
    public data class Disconnected public constructor(
        @SerialName(value = "error") public val errorMessage: String? = null
    ) : ServerConnection {

        @Transient
        override val status: ServerConnectionStatus = ServerConnectionStatus.Disconnected
    }

    public companion object
}

/**
 * Returns `true` if this [ServerConnection] is [ServerConnection.Connecting] or [ServerConnection.Disconnecting],
 * `false` otherwise.
 */
@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
public inline fun ServerConnection.isToggling(): Boolean {
    contract { returns(true) implies (this@isToggling is ServerConnection.Connecting || this@isToggling is ServerConnection.Disconnecting) }

    return this is ServerConnection.Connecting || this is ServerConnection.Disconnecting
}

/**
 * A convenience function for checking if this [ServerConnection] instance is [ServerConnection.Connecting].
 */
@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
public inline fun ServerConnection.isConnecting(): Boolean {
    contract { returns(true) implies (this@isConnecting is ServerConnection.Connecting) }

    return this is ServerConnection.Connecting
}

/**
 * A convenience function for checking if this [ServerConnection] instance is [ServerConnection.Disconnecting].
 */
@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
public inline fun ServerConnection.isDisconnecting(): Boolean {
    contract { returns(true) implies (this@isDisconnecting is ServerConnection.Disconnecting) }

    return this is ServerConnection.Disconnecting
}

/**
 * A convenience function for checking if this [ServerConnection] instance is [ServerConnection.Connected].
 */
@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
public inline fun ServerConnection.isConnected(): Boolean {
    contract { returns(true) implies (this@isConnected is ServerConnection.Connected) }

    return this is ServerConnection.Connected
}

/**
 * A convenience function for checking if this [ServerConnection] instance is [ServerConnection.Disconnected].
 */
@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
public inline fun ServerConnection.isDisconnected(): Boolean {
    contract { returns(true) implies (this@isDisconnected is ServerConnection.Disconnected) }

    return this is ServerConnection.Disconnected
}
