package com.mooncloak.vpn.app.android.api.server

import android.app.Activity
import android.content.Intent
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.logpile.core.info
import com.mooncloak.vpn.app.android.activity.MainActivity
import com.mooncloak.vpn.app.android.api.wireguard.AndroidWireGuardConnectionKeyPair
import com.mooncloak.vpn.app.android.api.wireguard.WireGuardTunnel
import com.mooncloak.vpn.app.android.api.wireguard.toWireGuardConfig
import com.mooncloak.vpn.app.shared.api.key.WireGuardConnectionKeyPairResolver
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkManager
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionRecordRepository
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnection
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.api.vpn.isConnected
import com.mooncloak.vpn.app.shared.api.vpn.isConnecting
import com.mooncloak.vpn.app.shared.api.vpn.isDisconnected
import com.mooncloak.vpn.app.shared.util.notification.NotificationManager
import com.mooncloak.vpn.app.shared.util.notification.cancelVPNNotification
import com.mooncloak.vpn.app.shared.util.notification.showVPNNotification
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.backend.Tunnel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.coroutines.resume
import kotlin.time.Duration.Companion.seconds

internal class AndroidVPNConnectionManager @Inject internal constructor(
    private val activity: Activity,
    private val serverConnectionRecordRepository: ServerConnectionRecordRepository,
    private val connectionKeyPairResolver: WireGuardConnectionKeyPairResolver,
    private val clock: Clock,
    private val localNetworkManager: LocalNetworkManager,
    private val notificationManager: NotificationManager
) : VPNConnectionManager {

    override val isActive: Boolean
        get() = coroutineScope.isActive && !isClosed

    override val connection: StateFlow<VPNConnection>
        get() = mutableConnection.asStateFlow()

    private val mutableConnection = MutableStateFlow<VPNConnection>(VPNConnection.Disconnected())

    private val backend = GoBackend(activity)

    private val connectedTunnels = mutableMapOf<String, WireGuardTunnel>()

    private val toggleMutex = Mutex(locked = false)
    private val emitMutex = Mutex(locked = false)

    private var coroutineScope: CoroutineScope = MainScope()
    private var isClosed = true
    private var job: Job? = null

    private var intentCallback: (() -> Unit)? = null

    override fun start() {
        if (isClosed && coroutineScope.isActive) {
            job?.cancel()

            isClosed = false

            job = coroutineScope.launch {
                eventLoop()
            }
        }
    }

    override fun cancel() {
        if (!isClosed) {
            isClosed = true
            job?.cancel()
        }
    }

    override suspend fun connect(server: Server) {
        toggleMutex.withLock {
            try {
                // We only support a single tunnel connection right now. So, if we are not currently disconnected,
                // disconnect before we connect.
                if (!connection.value.isDisconnected()) {
                    closeAllTunnels()
                }

                emit(
                    VPNConnection.Connecting(
                        server = server,
                        timestamp = clock.now()
                    )
                )

                // First we have to prepare the VPN Service if it wasn't already done.
                val intent = GoBackend.VpnService.prepare(activity)
                if (intent != null) {
                    LogPile.info(tag = TAG, message = "Preparing VPN Service.")

                    activity.startActivityForResult(intent, REQUEST_CODE)

                    suspendCancellableCoroutine { continuation ->
                        intentCallback = {
                            LogPile.info(tag = TAG, message = "Intent Callback.")

                            continuation.resume(Unit)

                            intentCallback = null
                        }
                    }
                }

                val localIpAddress = localNetworkManager.getInfo()?.ipAddress

                // FIXME: Cast
                val keyPair = connectionKeyPairResolver.resolve() as AndroidWireGuardConnectionKeyPair

                val wireGuardConfig = server.toWireGuardConfig(
                    keyPair = keyPair.keyPair,
                    localIpAddress = localIpAddress
                )
                val tunnel = WireGuardTunnel(
                    tunnelName = server.name,
                    server = server
                )

                withContext(Dispatchers.IO) {
                    backend.setState(
                        tunnel,
                        Tunnel.State.UP,
                        wireGuardConfig
                    )
                }

                connectedTunnels[tunnel.name] = tunnel

                try {
                    serverConnectionRecordRepository.upsert(
                        server = server,
                        lastConnected = clock.now()
                    )
                } catch (e: Exception) {
                    LogPile.error(tag = TAG, message = "Error saving VPN server connection record.", cause = e)
                }

                updateTunnels()
            } catch (e: Exception) {
                LogPile.error(message = "Error connecting to VPN server '${server.name}'.", cause = e)

                emit(VPNConnection.Disconnected(errorMessage = e.message, errorCause = e))
            }

            // TODO:
            // TODO: Save connection record
        }
    }

    override suspend fun disconnect() {
        toggleMutex.withLock {
            closeAllTunnels()
        }
    }

    internal fun receivedResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            // TODO: Verify success/error
            intentCallback?.invoke()
        }
    }

    private suspend fun closeAllTunnels() {
        emit(VPNConnection.Disconnecting(timestamp = clock.now()))

        withContext(Dispatchers.IO) {
            for (tunnelName in backend.runningTunnelNames) {
                val tunnel = connectedTunnels[tunnelName] ?: WireGuardTunnel(tunnelName = tunnelName)

                LogPile.info(tag = TAG, message = "Disconnecting from tunnel '${tunnel.tunnelName}'.")

                try {
                    backend.setState(
                        tunnel,
                        Tunnel.State.DOWN,
                        null
                    )
                } catch (e: Exception) {
                    LogPile.error(
                        tag = TAG,
                        message = "Error disconnecting from tunnel '${tunnel.tunnelName}'.",
                        cause = e
                    )
                }
            }
        }

        connectedTunnels.clear()

        emit(VPNConnection.Disconnected())
    }

    private suspend fun updateTunnels() {
        withContext(Dispatchers.IO) {
            val updatedTunnels = mutableMapOf<String, WireGuardTunnel>()

            LogPile.info(tag = TAG, message = "updatedTunnels: running tunnels = ${backend.runningTunnelNames}")

            for (tunnelName in backend.runningTunnelNames) {
                val tunnel = connectedTunnels[tunnelName] ?: WireGuardTunnel(tunnelName = tunnelName)

                val state = backend.getState(tunnel)
                val stats = backend.getStatistics(tunnel)

                tunnel.onStateChange(state)
                tunnel.onStatisticsChanged(stats)

                updatedTunnels[tunnelName] = tunnel
            }

            connectedTunnels.clear()
            connectedTunnels.putAll(updatedTunnels)

            val currentConnection = mutableConnection.value

            val updatedConnection = when {
                connectedTunnels.isNotEmpty() -> VPNConnection.Connected(
                    tunnels = connectedTunnels.values.toList(),
                    timestamp = if (currentConnection.isConnected()) {
                        currentConnection.timestamp
                    } else {
                        clock.now()
                    }
                )

                mutableConnection.value.isConnecting() -> mutableConnection.value

                else -> VPNConnection.Disconnected()
            }

            emit(updatedConnection)
        }
    }

    private suspend fun emit(connection: VPNConnection) {
        emitMutex.withLock {
            withContext(Dispatchers.Main) {
                LogPile.info(tag = TAG, message = "Emitting updated connection: $connection")

                if (connection.isConnected()) {
                    notificationManager.showVPNNotification(
                        context = activity,
                        openAppIntent = Intent(activity, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    )
                } else {
                    notificationManager.cancelVPNNotification()
                }

                mutableConnection.value = connection
            }
        }
    }

    private suspend fun eventLoop() {
        while (this.isActive) {
            updateTunnels()

            // Arbitrarily chosen time period to poll for the latest tunnels
            delay(15.seconds)
        }
    }

    internal companion object {

        private const val TAG: String = "VPNConnectionManager"
        internal const val REQUEST_CODE: Int = 123456
    }
}
