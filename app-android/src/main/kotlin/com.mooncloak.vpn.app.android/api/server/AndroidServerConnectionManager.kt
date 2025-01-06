package com.mooncloak.vpn.app.android.api.server

import android.app.Activity
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
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
import kotlinx.datetime.Clock

internal class AndroidServerConnectionManager @Inject internal constructor(
    private val context: Activity,
    private val api: MooncloakVpnServiceHttpApi,
    private val subscriptionStorage: SubscriptionStorage,
    private val connectionKeyPairResolver: WireGuardConnectionKeyPairResolver,
    private val clock: Clock
) : ServerConnectionManager {

    override val connection: StateFlow<ServerConnection>
        get() = mutableConnection.asStateFlow()

    private val mutableConnection = MutableStateFlow<ServerConnection>(ServerConnection.Disconnected())

    private val backend = GoBackend(context)

    override suspend fun connect(server: Server) {
        try {
            mutableConnection.value = ServerConnection.Connecting(
                server = server,
                timestamp = clock.now()
            )

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

            mutableConnection.value = ServerConnection.Connected(
                sessionId = "", // TODO
                server = server,
                timestamp = clock.now()
            )
        } catch (e: Exception) {
            LogPile.error(message = "Error connecting to VPN server '${server.name}'.", cause = e)

            mutableConnection.value = ServerConnection.Disconnected(errorMessage = e.message)
        }

        // TODO:
        // TODO: Save connection record
    }

    override suspend fun disconnect() {
        try {
            backend.runningTunnelNames.map { tunnelName ->
                WireGuardTunnel(name = tunnelName)
            }.forEach { tunnel ->
                mutableConnection.value = ServerConnection.Disconnecting(timestamp = clock.now())

                backend.setState(
                    tunnel,
                    Tunnel.State.DOWN,
                    null
                )
            }
        } catch (e: Exception) {
            LogPile.error(message = "Error disconnecting from VPN servers.", cause = e)

            // TODO: What if we are still connected?
            mutableConnection.value = ServerConnection.Disconnected(
                errorMessage = e.message
            )
        }
    }

    internal companion object {

        internal const val REQUEST_CODE: Int = 0
    }
}
