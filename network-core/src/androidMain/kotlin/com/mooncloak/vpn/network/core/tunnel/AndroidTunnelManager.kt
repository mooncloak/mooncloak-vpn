package com.mooncloak.vpn.network.core.tunnel

import android.content.Context
import android.content.Intent
import com.mooncloak.vpn.api.shared.server.Server
import kotlinx.coroutines.flow.StateFlow

public actual interface TunnelManager {

    public actual val tunnels: StateFlow<List<Tunnel>>

    public actual suspend fun sync()

    public actual suspend fun connect(server: Server): Tunnel?

    public actual suspend fun disconnect(tunnelName: String)

    public actual suspend fun disconnectAll()

    /**
     * Prepares the underlying VPN [TunnelManager] service if it is not already prepared.
     *
     * @param [context] The [Context] used to prepare the underlying VPN [TunnelManager] service. Ideally this should
     * be an Activity [Context], but if it is not, then the implementation should handle it properly (ex: start an
     * Activity to handle it, or return `false`).
     */
    public fun prepare(context: Context): Intent?

    public actual companion object
}
