package com.mooncloak.vpn.app.shared.api.wireguard

import com.mooncloak.vpn.api.shared.preference.WireGuardPreferences
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.app.shared.api.key.WireGuardConnectionKeyPairResolver
import com.mooncloak.vpn.data.shared.provider.Provider
import com.mooncloak.vpn.network.core.tunnel.IosTunnel
import com.mooncloak.vpn.network.core.tunnel.TunnelManager
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

public abstract class IosWireGuardTunnelManager public constructor(
    private val coroutineScope: ApplicationCoroutineScope
) : TunnelManager {

    override val tunnels: StateFlow<List<IosTunnel>>
        get() = mutableTunnels.asStateFlow()

    private val mutableTunnels = MutableStateFlow<List<IosTunnel>>(emptyList())

    abstract override suspend fun sync()

    abstract override suspend fun connect(server: Server): IosTunnel?

    abstract override suspend fun disconnect(tunnelName: String)

    abstract override suspend fun disconnectAll()

    public fun updateTunnels(tunnels: List<IosTunnel>) {
        mutableTunnels.value = tunnels
    }

    public interface Factory {

        public fun create(
            coroutineScope: ApplicationCoroutineScope,
            connectionKeyPairResolver: WireGuardConnectionKeyPairResolver,
            preferenceProvider: Provider<WireGuardPreferences?>
        ): IosWireGuardTunnelManager
    }
}
