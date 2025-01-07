package com.mooncloak.vpn.app.android.api.server

import android.app.Activity
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.logpile.core.info
import com.mooncloak.vpn.app.android.api.wireguard.AndroidWireGuardConnectionKeyPair
import com.mooncloak.vpn.app.android.api.wireguard.WireGuardTunnel
import com.mooncloak.vpn.app.android.api.wireguard.toWireGuardConfig
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.key.WireGuardConnectionKeyPairResolver
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkManager
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnection
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.backend.Tunnel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock

internal class AndroidVPNConnectionManager @Inject internal constructor(
    private val context: Activity,
    private val api: MooncloakVpnServiceHttpApi,
    private val subscriptionStorage: SubscriptionStorage,
    private val connectionKeyPairResolver: WireGuardConnectionKeyPairResolver,
    private val clock: Clock,
    private val localNetworkManager: LocalNetworkManager
) : VPNConnectionManager {

    override val connection: StateFlow<VPNConnection>
        get() = mutableConnection.asStateFlow()

    private val mutableConnection = MutableStateFlow<VPNConnection>(VPNConnection.Disconnected())

    private val backend = GoBackend(context)

    private val connectedTunnels = mutableMapOf<String, com.mooncloak.vpn.app.shared.api.vpn.Tunnel>()

    override suspend fun connect(server: Server) {
        try {
            mutableConnection.value = VPNConnection.Connecting(
                server = server,
                timestamp = clock.now()
            )

            // First we have to prepare the VPN Service if it wasn't already done.
            val intent = GoBackend.VpnService.prepare(context)
            if (intent != null) {
                context.startActivityForResult(intent, REQUEST_CODE)
            }

            val localIpAddress = localNetworkManager.getInfo()?.ipAddress

            // FIXME: Cast
            val keyPair = connectionKeyPairResolver.resolve() as AndroidWireGuardConnectionKeyPair

            val wireGuardConfig = server.toWireGuardConfig(
                keyPair = keyPair.keyPair,
                localIpAddress = localIpAddress
            )
            val tunnel = WireGuardTunnel(name = server.name)

            backend.setState(
                tunnel,
                Tunnel.State.UP,
                wireGuardConfig
            )

            mutableConnection.value = VPNConnection.Connected(
                sessionId = "", // TODO
                server = server,
                timestamp = clock.now()
            )
        } catch (e: Exception) {
            LogPile.error(message = "Error connecting to VPN server '${server.name}'.", cause = e)

            mutableConnection.value = VPNConnection.Disconnected(errorMessage = e.message)
        }

        // TODO:
        // TODO: Save connection record
    }

    override suspend fun disconnect() {
        try {
            mutableConnection.value = VPNConnection.Disconnecting(timestamp = clock.now())

            backend.runningTunnelNames.map { tunnelName ->
                WireGuardTunnel(name = tunnelName)
            }.forEach { tunnel ->
                LogPile.info(message = "disconnect: tunnel: ${tunnel.name}")

                backend.setState(
                    tunnel,
                    Tunnel.State.DOWN,
                    null
                )
            }
        } catch (e: Exception) {
            LogPile.error(message = "Error disconnecting from VPN servers.", cause = e)

            // TODO: What if we are still connected?
            mutableConnection.value = VPNConnection.Disconnected(
                errorMessage = e.message
            )
        }
    }

    private suspend fun test() {
        backend.runningTunnelNames.map { tunnelName ->
            WireGuardTunnel(name = tunnelName)
        }.forEach { tunnel ->
            backend.getState(tunnel)
            backend.getStatistics(tunnel)
        }
    }

    internal companion object {

        internal const val REQUEST_CODE: Int = 0
    }
}
