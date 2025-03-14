package com.mooncloak.vpn.network.core.tunnel

import com.mooncloak.vpn.api.shared.server.Server
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

public actual interface TunnelManager {

    public actual val tunnels: StateFlow<List<Tunnel>>

    public actual suspend fun sync()

    public actual suspend fun connect(server: Server): Tunnel?

    public actual suspend fun disconnect(tunnelName: String)

    public actual suspend fun disconnectAll()

    public actual fun subscribeToChanges(
        coroutineScope: CoroutineScope,
        poll: Duration
    ): Job

    public actual companion object
}
