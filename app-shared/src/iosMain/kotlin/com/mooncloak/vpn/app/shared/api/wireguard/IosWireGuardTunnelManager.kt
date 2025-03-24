package com.mooncloak.vpn.app.shared.api.wireguard

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.network.core.tunnel.Tunnel
import com.mooncloak.vpn.network.core.tunnel.TunnelManager
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class IosWireGuardTunnelManager @Inject internal constructor(
    private val coroutineScope: ApplicationCoroutineScope
) : TunnelManager {

    override val tunnels: StateFlow<List<Tunnel>>
        get() = mutableTunnels.asStateFlow()

    private val mutableTunnels = MutableStateFlow<List<Tunnel>>(emptyList())

    override suspend fun sync() {
        TODO("Not yet implemented")
    }

    override suspend fun connect(server: Server): Tunnel? {
        TODO("Not yet implemented")
    }

    override suspend fun disconnect(tunnelName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun disconnectAll() {
        TODO("Not yet implemented")
    }
}
