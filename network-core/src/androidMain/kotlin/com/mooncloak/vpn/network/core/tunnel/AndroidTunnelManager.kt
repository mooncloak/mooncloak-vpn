package com.mooncloak.vpn.network.core.tunnel

import android.content.Context
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
     *
     * @return `true` if the VPN [TunnelManager] is prepared and can be used to connect tunnels, `false` otherwise.
     * Note that if this function returns `false`, then calling to any of the other functions might result in
     * exceptions being thrown.
     */
    public suspend fun prepare(context: Context): Boolean

    /**
     * Signals that the external preparation of the underlying VPN service has been handled. The [TunnelManager]
     * implementation will then resume the previously called [prepare] function with the current state of the
     * preparation.
     */
    public fun finishedPreparation()

    public actual companion object
}
