package com.mooncloak.vpn.network.core.tunnel

import com.mooncloak.vpn.api.shared.server.Server
import kotlinx.coroutines.flow.StateFlow

public actual interface TunnelManager {

    public actual val tunnels: StateFlow<List<Tunnel>>

    public actual suspend fun sync()

    public actual suspend fun connect(server: Server): Tunnel?

    public actual suspend fun disconnect(tunnelName: String)

    public actual suspend fun disconnectAll()

    public actual companion object
}
