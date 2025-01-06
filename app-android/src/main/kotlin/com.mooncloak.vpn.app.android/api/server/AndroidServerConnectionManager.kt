package com.mooncloak.vpn.app.android.api.server

import android.app.Activity
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.android.api.wireguard.AndroidWireGuardConnectionKeyPair
import com.mooncloak.vpn.app.android.api.wireguard.WireGuardTunnel
import com.mooncloak.vpn.app.android.api.wireguard.toWireGuardConfig
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.key.WireGuardConnectionKeyPairResolver
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnection
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionManager
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.backend.Tunnel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class AndroidServerConnectionManager @Inject internal constructor(
    private val context: Activity,
    private val api: MooncloakVpnServiceHttpApi,
    private val subscriptionStorage: SubscriptionStorage,
    private val connectionKeyPairResolver: WireGuardConnectionKeyPairResolver
) : ServerConnectionManager {

    override val connection: StateFlow<ServerConnection>
        get() = mutableConnection.asStateFlow()

    private val mutableConnection = MutableStateFlow(ServerConnection.Disconnected)

    private val backend = GoBackend(context)

    override suspend fun connect(server: Server) {
        // First we have to prepare the VPN Service if it wasn't already done.
        val intent = GoBackend.VpnService.prepare(context)
        if (intent != null) {
            context.startActivityForResult(intent, REQUEST_CODE)
        }

        // FIXME: Cast
        val keyPair = connectionKeyPairResolver.resolve() as AndroidWireGuardConnectionKeyPair

        val wireGuardConfig = server.toWireGuardConfig(keyPair.keyPair)
        val tunnel = WireGuardTunnel(name = server.name)

        backend.setState(
            tunnel,
            Tunnel.State.UP,
            wireGuardConfig
        )

        // TODO:
        // TODO: Save connection record
    }

    override suspend fun disconnect() {
        backend.runningTunnelNames.map { tunnelName ->
            WireGuardTunnel(name = tunnelName)
        }.forEach { tunnel ->
            backend.setState(
                tunnel,
                Tunnel.State.DOWN,
                null
            )
        }
    }

    internal companion object {

        internal const val REQUEST_CODE: Int = 0
    }
}
