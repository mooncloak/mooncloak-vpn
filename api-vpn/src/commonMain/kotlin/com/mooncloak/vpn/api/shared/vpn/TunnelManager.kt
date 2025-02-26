package com.mooncloak.vpn.api.shared.vpn

import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.VPNConnectionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public interface TunnelManager {

    public val tunnels: StateFlow<List<Tunnel>>

    public suspend fun sync()

    public suspend fun connect(tunnel: Tunnel)

    public suspend fun connect(server: Server, tunnelName: String = server.name)

    public suspend fun disconnect(tunnel: Tunnel)

    public suspend fun disconnect(tunnelName: String)

    public suspend fun disconnectAll()

    public fun subscribeToChanges(
        coroutineScope: CoroutineScope,
        poll: Duration = 5.seconds
    ): Job

    public companion object
}

public val TunnelManager.connectedTunnels: Flow<List<Tunnel>>
    inline get() = tunnels.map { tunnels ->
        tunnels.filter { tunnel -> tunnel.status.value == VPNConnectionStatus.Connected }
    }

/**
 * Determines if there currently is an active tunnel connection running.
 */
public val TunnelManager.isActive: Boolean
    get() = tunnels.value.any { tunnel -> tunnel.status.value == VPNConnectionStatus.Connected }
