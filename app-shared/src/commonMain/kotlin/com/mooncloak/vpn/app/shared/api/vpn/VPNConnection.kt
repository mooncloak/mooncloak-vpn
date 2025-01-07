package com.mooncloak.vpn.app.shared.api.vpn

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionStatus
import kotlinx.datetime.Instant
import kotlinx.serialization.Transient
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Immutable
public sealed interface VPNConnection {

    public val status: ServerConnectionStatus

    @Immutable
    public data class Connecting public constructor(
        public val server: Server,
        public val timestamp: Instant
    ) : VPNConnection {

        @Transient
        override val status: ServerConnectionStatus = ServerConnectionStatus.Connecting
    }

    @Immutable
    public data class Connected public constructor(
        public val tunnels: List<Tunnel> = emptyList(),
        public val sessionId: String,
        public val server: Server,
        public val timestamp: Instant,
        public val rxThroughput: Long? = null,
        public val txThroughput: Long? = null,
        public val totalRx: Long? = null,
        public val totalTx: Long? = null
    ) : VPNConnection {

        @Transient
        override val status: ServerConnectionStatus = ServerConnectionStatus.Connected
    }

    @Immutable
    public data class Disconnecting public constructor(
        public val server: Server? = null,
        public val timestamp: Instant
    ) : VPNConnection {

        @Transient
        override val status: ServerConnectionStatus = ServerConnectionStatus.Disconnecting
    }

    @Immutable
    public data class Disconnected public constructor(
        public val errorMessage: String? = null,
        public val cause: Throwable? = null
    ) : VPNConnection {

        @Transient
        override val status: ServerConnectionStatus = ServerConnectionStatus.Disconnected
    }

    public companion object
}

/**
 * Returns `true` if this [VPNConnection] is [VPNConnection.Connecting] or [VPNConnection.Disconnecting],
 * `false` otherwise.
 */
@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
public inline fun VPNConnection.isToggling(): Boolean {
    contract { returns(true) implies (this@isToggling is VPNConnection.Connecting || this@isToggling is VPNConnection.Disconnecting) }

    return this is VPNConnection.Connecting || this is VPNConnection.Disconnecting
}

/**
 * A convenience function for checking if this [VPNConnection] instance is [VPNConnection.Connecting].
 */
@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
public inline fun VPNConnection.isConnecting(): Boolean {
    contract { returns(true) implies (this@isConnecting is VPNConnection.Connecting) }

    return this is VPNConnection.Connecting
}

/**
 * A convenience function for checking if this [VPNConnection] instance is [VPNConnection.Disconnecting].
 */
@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
public inline fun VPNConnection.isDisconnecting(): Boolean {
    contract { returns(true) implies (this@isDisconnecting is VPNConnection.Disconnecting) }

    return this is VPNConnection.Disconnecting
}

/**
 * A convenience function for checking if this [VPNConnection] instance is [VPNConnection.Connected].
 */
@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
public inline fun VPNConnection.isConnected(): Boolean {
    contract { returns(true) implies (this@isConnected is VPNConnection.Connected) }

    return this is VPNConnection.Connected
}

/**
 * A convenience function for checking if this [VPNConnection] instance is [VPNConnection.Disconnected].
 */
@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalContracts::class)
public inline fun VPNConnection.isDisconnected(): Boolean {
    contract { returns(true) implies (this@isDisconnected is VPNConnection.Disconnected) }

    return this is VPNConnection.Disconnected
}
