package com.mooncloak.vpn.api.shared.vpn

import com.mooncloak.vpn.api.shared.server.Server
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

/**
 * A component that listens to [VPNConnection] changes and provides functions to connect to and disconnect from VPN
 * servers.
 */
public interface VPNConnectionManager : AutoCloseable {

    /**
     * If this [VPNConnectionManager] is active and can update its [connection] and perform its tasks.
     */
    public val isActive: Boolean

    /**
     * A [StateFlow] of the current [VPNConnection] state.
     */
    public val connection: StateFlow<VPNConnection>

    /**
     * Starts listening to [connection] changes. Note that an implementation may maintain its own [CoroutineScope] or
     * use an application [CoroutineScope]. In the latter case, this function might not do anything if the underlying
     * [CoroutineScope] is not active.
     */
    public fun start()

    /**
     * Stops listening to [connection] changes.
     */
    public fun cancel()

    override fun close() {
        cancel()
    }

    /**
     * Attempts to connect to the provided VPN [server].
     */
    public suspend fun connect(server: Server)

    /**
     * Attempts to disconnect from any VPN servers.
     */
    public suspend fun disconnect()

    public companion object
}
