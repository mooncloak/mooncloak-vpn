package com.mooncloak.vpn.api.shared.vpn

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.VPNConnectionStatus
import kotlinx.datetime.Instant
import kotlinx.serialization.Transient
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@Immutable
public sealed interface VPNConnection {

    public val status: VPNConnectionStatus
    public val timestamp: Instant?

    @Immutable
    public data class Connecting public constructor(
        public override val timestamp: Instant,
        public val server: Server
    ) : VPNConnection {

        @Transient
        override val status: VPNConnectionStatus = VPNConnectionStatus.Connecting
    }

    @Immutable
    public data class Connected public constructor(
        public override val timestamp: Instant,
        public val tunnels: List<Tunnel> = emptyList()
    ) : VPNConnection {

        @Transient
        override val status: VPNConnectionStatus = VPNConnectionStatus.Connected
    }

    @Immutable
    public data class Disconnecting public constructor(
        public override val timestamp: Instant,
        public val server: Server? = null
    ) : VPNConnection {

        @Transient
        override val status: VPNConnectionStatus = VPNConnectionStatus.Disconnecting
    }

    @Immutable
    public data class Disconnected public constructor(
        public override val timestamp: Instant? = null,
        public val errorMessage: String? = null,
        public val errorCause: Throwable? = null
    ) : VPNConnection {

        @Transient
        override val status: VPNConnectionStatus = VPNConnectionStatus.Disconnected
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

/**
 * Determines if this [VPNConnection] is [VPNConnection.Connected] with a tunnel connected to the provided [server]
 * instance.
 */
@OptIn(ExperimentalContracts::class)
public fun VPNConnection.connectedTo(server: Server): Boolean {
    contract { returns(true) implies (this@connectedTo is VPNConnection.Connected) }

    return if (this is VPNConnection.Connected) {
        this.tunnels.any { tunnel -> tunnel.server == server }
    } else {
        false
    }
}

/**
 * Retrieves the default [Tunnel] for this connection.
 */
public val VPNConnection.defaultTunnel: Tunnel?
    inline get() = if (this is VPNConnection.Connected) {
        this.tunnels.firstOrNull { tunnel -> tunnel.server != null } ?: this.tunnels.firstOrNull()
    } else {
        null
    }
