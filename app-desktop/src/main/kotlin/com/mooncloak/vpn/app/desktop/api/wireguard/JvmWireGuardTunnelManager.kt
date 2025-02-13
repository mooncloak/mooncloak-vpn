package com.mooncloak.vpn.app.desktop.api.wireguard

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.vpn.Tunnel
import com.mooncloak.vpn.app.shared.api.vpn.TunnelManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

internal class JvmWireGuardTunnelManager @Inject internal constructor(

) : TunnelManager {

    override val tunnels: StateFlow<List<Tunnel>>
        get() = TODO("Not yet implemented")

    override suspend fun sync() {
        TODO("Not yet implemented")
    }

    override suspend fun connect(tunnel: Tunnel) {
        TODO("Not yet implemented")
    }

    override suspend fun connect(server: Server, tunnelName: String) {
        TODO("Not yet implemented")
    }

    override suspend fun disconnect(tunnel: Tunnel) {
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
