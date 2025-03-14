package com.mooncloak.vpn.app.shared.api.wireguard

import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.network.core.tunnel.Tunnel
import com.mooncloak.vpn.network.core.tunnel.TunnelManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

public operator fun TunnelManager.Companion.invoke(): TunnelManager = IosWireGuardTunnelManager()

internal class IosWireGuardTunnelManager internal constructor(

) : TunnelManager {

    override val tunnels: StateFlow<List<Tunnel>>
        get() = TODO("Not yet implemented")

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

    override fun subscribeToChanges(coroutineScope: CoroutineScope, poll: Duration): Job {
        TODO("Not yet implemented")
    }
}
