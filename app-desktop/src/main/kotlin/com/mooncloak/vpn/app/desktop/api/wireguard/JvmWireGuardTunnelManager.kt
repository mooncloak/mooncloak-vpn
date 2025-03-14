package com.mooncloak.vpn.app.desktop.api.wireguard

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.network.core.tunnel.Tunnel
import com.mooncloak.vpn.network.core.tunnel.TunnelManager
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration

internal class JvmWireGuardTunnelManager @Inject internal constructor(
    private val coroutineScope: ApplicationCoroutineScope
) : TunnelManager {

    override val tunnels: StateFlow<List<Tunnel>>
        get() = mutableTunnels.asStateFlow()

    private val mutableTunnels = MutableStateFlow<List<Tunnel>>(emptyList())

    override suspend fun sync() {
    }

    override suspend fun connect(server: Server): Tunnel? = null

    override suspend fun disconnect(tunnelName: String) {
    }

    override suspend fun disconnectAll() {
    }

    override fun subscribeToChanges(coroutineScope: CoroutineScope, poll: Duration): Job =
        coroutineScope.launch {
            // TODO
        }
}
