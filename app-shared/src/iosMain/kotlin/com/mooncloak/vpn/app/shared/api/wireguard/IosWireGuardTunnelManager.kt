package com.mooncloak.vpn.app.shared.api.wireguard

import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.network.core.tunnel.Tunnel
import com.mooncloak.vpn.network.core.tunnel.TunnelManager
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

public abstract class IosWireGuardTunnelManager public constructor(
    private val coroutineScope: ApplicationCoroutineScope
) : TunnelManager {

    override val tunnels: StateFlow<List<Tunnel>>
        get() = mutableTunnels.asStateFlow()

    private val mutableTunnels = MutableStateFlow<List<Tunnel>>(emptyList())

    abstract override suspend fun sync()

    abstract override suspend fun connect(server: Server): Tunnel?

    abstract override suspend fun disconnect(tunnelName: String)

    abstract override suspend fun disconnectAll()

    public fun updateTunnels(tunnels: List<Tunnel>) {
        mutableTunnels.value = tunnels
    }

    public interface Factory {

        public fun create(coroutineScope: ApplicationCoroutineScope): IosWireGuardTunnelManager
    }
}
