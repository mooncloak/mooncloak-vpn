package com.mooncloak.vpn.network.core.tunnel

import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.VPNConnectionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * A component that manages VPN tunnels between the device running this application and other peers.
 */
public expect interface TunnelManager {

    /**
     * A [Flow] of the [List] of [Tunnel]s managed by this [TunnelManager]. Note that this might include inactive and
     * not connected [Tunnel]s.
     */
    public val tunnels: StateFlow<List<Tunnel>>

    /**
     * Synchronizes the state with any underlying resources. This forces a refresh and retrieves the latest state
     * values and emits the any changes on the [tunnels] flow.
     */
    public suspend fun sync()

    /**
     * Creates a [Tunnel] connection to the provided [server].
     *
     * @param [server] The VPN [Server] to create establish a VPN [Tunnel] connection with.
     *
     * @return The established [Tunnel] to the [server], or `null` if the [Tunnel] could not be established.
     */
    public suspend fun connect(server: Server): Tunnel?

    /**
     * Disconnects from the [Tunnel] identified by the provided [tunnelName] if the connection exists.
     *
     * @param [tunnelName] The unique name of the [Tunnel] that should be disconnected from.
     */
    public suspend fun disconnect(tunnelName: String)

    /**
     * Disconnects from all currently connected [Tunnel]s managed by this [TunnelManager].
     */
    public suspend fun disconnectAll()

    public companion object
}

/**
 * Retrieves a [Flow] of the [List] of actively connected [Tunnel]s.
 */
public val TunnelManager.connectedTunnels: Flow<List<Tunnel>>
    inline get() = tunnels.map { tunnels ->
        tunnels.filter { tunnel -> tunnel.status.value == VPNConnectionStatus.Connected }
    }

/**
 * Determines if there currently is an active tunnel connection running.
 */
public val TunnelManager.isConnected: Boolean
    get() = tunnels.value.any { tunnel -> tunnel.status.value == VPNConnectionStatus.Connected }

/**
 * Disconnects from the [Tunnel] representing the connection to the provided [server] if it exists.
 *
 * @param [server] The [Server] whose corresponding tunnel should be disconnected from if it exists.
 */
public suspend inline fun TunnelManager.disconnect(server: Server) {
    this.disconnect(tunnelName = server.name)
}

/**
 * Disconnects from the provided [Tunnel] if the connection exists.
 *
 * @param [tunnel] The [Tunnel] to disconnect from if the connection exists.
 */
public suspend inline fun TunnelManager.disconnect(tunnel: Tunnel) {
    this.disconnect(tunnelName = tunnel.tunnelName)
}
