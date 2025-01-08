package com.mooncloak.vpn.app.android.api.server

import android.app.Activity
import android.content.Intent
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.logpile.core.info
import com.mooncloak.vpn.app.android.activity.MainActivity
import com.mooncloak.vpn.app.android.receiver.DisconnectTunnelsBroadcastReceiver
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionRecordRepository
import com.mooncloak.vpn.app.shared.api.vpn.TunnelManager
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnection
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.api.vpn.connectedTunnels
import com.mooncloak.vpn.app.shared.api.vpn.isConnected
import com.mooncloak.vpn.app.shared.api.vpn.isConnecting
import com.mooncloak.vpn.app.shared.api.vpn.isDisconnected
import com.mooncloak.vpn.app.shared.util.ApplicationContext
import com.mooncloak.vpn.app.shared.util.notification.NotificationManager
import com.mooncloak.vpn.app.shared.util.notification.cancelVPNNotification
import com.mooncloak.vpn.app.shared.util.notification.showVPNNotification
import com.wireguard.android.backend.GoBackend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.coroutines.resume

internal class AndroidVPNConnectionManager @Inject internal constructor(
    private val activity: Activity,
    private val context: ApplicationContext,
    private val serverConnectionRecordRepository: ServerConnectionRecordRepository,
    private val clock: Clock,
    private val notificationManager: NotificationManager,
    private val tunnelManager: TunnelManager
) : VPNConnectionManager {

    override val isActive: Boolean
        get() = coroutineScope.isActive && !isClosed

    override val connection: StateFlow<VPNConnection>
        get() = mutableConnection.asStateFlow()

    private val mutableConnection = MutableStateFlow<VPNConnection>(VPNConnection.Disconnected())

    private val toggleMutex = Mutex(locked = false)
    private val emitMutex = Mutex(locked = false)

    private var coroutineScope: CoroutineScope = MainScope()
    private var isClosed = true
    private var changesJob: Job? = null
    private var tunnelsJob: Job? = null

    private var intentCallback: (() -> Unit)? = null

    override fun start() {
        if (isClosed && coroutineScope.isActive) {
            changesJob?.cancel()
            tunnelsJob?.cancel()

            isClosed = false

            changesJob = tunnelManager.subscribeToChanges(coroutineScope = coroutineScope)
            tunnelsJob = tunnelManager.connectedTunnels
                .map { tunnels ->
                    val currentConnection = mutableConnection.value

                    when {
                        tunnels.isNotEmpty() -> VPNConnection.Connected(
                            tunnels = tunnels.toList(),
                            timestamp = if (currentConnection.isConnected()) {
                                currentConnection.timestamp
                            } else {
                                clock.now()
                            }
                        )

                        mutableConnection.value.isConnecting() -> mutableConnection.value

                        else -> VPNConnection.Disconnected()
                    }
                }
                .onEach { connection -> emit(connection) }
                .catch { e ->
                    LogPile.error(
                        tag = TAG,
                        message = "Error listening to connected tunnel changes from TunnelManager.",
                        cause = e
                    )
                }
                .launchIn(coroutineScope)
        }
    }

    override fun cancel() {
        if (!isClosed) {
            isClosed = true
            changesJob?.cancel()
            tunnelsJob?.cancel()
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
                val intent = GoBackend.VpnService.prepare(context)
                if (intent != null) {
                    LogPile.info(tag = TAG, message = "Preparing VPN Service.")

                    activity.startActivityForResult(intent, REQUEST_CODE)

                    // FIXME: The callback isn't working.
                    suspendCancellableCoroutine { continuation ->
                        intentCallback = {
                            LogPile.info(tag = TAG, message = "Intent Callback.")

                            continuation.resume(Unit)

                            intentCallback = null
                        }
                    }
                }

                tunnelManager.connect(server = server)

                try {
                    serverConnectionRecordRepository.upsert(
                        server = server,
                        lastConnected = clock.now()
                    )
                } catch (e: Exception) {
                    LogPile.error(tag = TAG, message = "Error saving VPN server connection record.", cause = e)
                }
            } catch (e: Exception) {
                LogPile.error(message = "Error connecting to VPN server '${server.name}'.", cause = e)

                emit(VPNConnection.Disconnected(errorMessage = e.message, errorCause = e))
            }
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

        tunnelManager.disconnectAll()

        emit(VPNConnection.Disconnected())
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
                        },
                        disconnectIntent = Intent(context, DisconnectTunnelsBroadcastReceiver::class.java).apply {
                            action = DisconnectTunnelsBroadcastReceiver.ACTION
                        }
                    )
                } else {
                    notificationManager.cancelVPNNotification()
                }

                mutableConnection.value = connection
            }
        }
    }

    internal companion object {

        private const val TAG: String = "VPNConnectionManager"
        internal const val REQUEST_CODE: Int = 123456
    }
}
