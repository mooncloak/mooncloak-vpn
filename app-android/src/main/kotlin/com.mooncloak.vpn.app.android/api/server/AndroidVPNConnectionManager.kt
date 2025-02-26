package com.mooncloak.vpn.app.android.api.server

import android.app.Activity
import android.content.Intent
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.logpile.core.info
import com.mooncloak.kodetools.logpile.core.warning
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.ServerConnectionRecord
import com.mooncloak.vpn.api.shared.server.ServerConnectionRecordRepository
import com.mooncloak.vpn.api.shared.vpn.TunnelManager
import com.mooncloak.vpn.api.shared.vpn.VPNConnection
import com.mooncloak.vpn.api.shared.vpn.VPNConnectionManager
import com.mooncloak.vpn.api.shared.vpn.connectedTunnels
import com.mooncloak.vpn.api.shared.vpn.isConnected
import com.mooncloak.vpn.api.shared.vpn.isConnecting
import com.mooncloak.vpn.api.shared.vpn.isDisconnected
import com.mooncloak.vpn.app.android.service.MooncloakVpnService
import com.mooncloak.vpn.app.shared.util.ApplicationContext
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

internal class AndroidVPNConnectionManager @Inject internal constructor(
    private val activity: Activity,
    private val context: ApplicationContext,
    private val serverConnectionRecordRepository: ServerConnectionRecordRepository,
    private val clock: Clock,
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

    // Arbitrarily chosen time period.
    private val maxConnectionPeriod = 15.seconds
    private var lastConnectionTime: Instant? = null

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
                .onEach { connection ->
                    emit(connection)

                    // Attempt to check if we have been connecting for too long. If we have been attempting to connect,
                    // unsuccessfully for an extended period of time, cancel the connection. This is an attempt to
                    // prevent the user getting stuck in an invalid state.
                    // TODO: Should this be the responsibility of the ViewModel layer? The reason I didn't put it there
                    // initially is that this component is used by numerous ViewModels and I didn't want to duplicate
                    // the logic.
                    if (
                        connection.isConnecting() &&
                        ((lastConnectionTime?.let { clock.now() - it } ?: 0.seconds) > maxConnectionPeriod)
                    ) {
                        LogPile.warning(
                            tag = TAG,
                            message = "Failed to connect for $maxConnectionPeriod. Forcing a disconnect."
                        )

                        disconnect()
                    } else if (connection.isConnected() || connection.isDisconnected()) {
                        // If we are connected or disconnected, reset the last connection time. The last connection
                        // time value is only set on an explicit connect action.
                        lastConnectionTime = null
                    }
                }
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

                val now = clock.now()

                lastConnectionTime = now

                emit(
                    VPNConnection.Connecting(
                        server = server,
                        timestamp = now
                    )
                )

                // First we have to prepare the VPN Service if it wasn't already done.
                val intent = GoBackend.VpnService.prepare(context)
                if (intent != null) {
                    LogPile.info(tag = TAG, message = "Preparing VPN Service.")

                    activity.startActivityForResult(intent, MooncloakVpnService.RequestCode.PREPARE)

                    // FIXME: The callback isn't working.
                    /*
                    suspendCancellableCoroutine { continuation ->
                        intentCallback = {
                            LogPile.info(tag = TAG, message = "Intent Callback.")

                            continuation.resume(Unit)

                            intentCallback = null
                        }
                    }*/
                }

                tunnelManager.connect(server = server)

                try {
                    serverConnectionRecordRepository.upsert(
                        id = server.id,
                        insert = {
                            ServerConnectionRecord(
                                server = server,
                                lastConnected = clock.now()
                            )
                        },
                        update = {
                            this.copy(
                                server = server,
                                lastConnected = clock.now()
                            )
                        }
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
            lastConnectionTime = null

            closeAllTunnels()
        }
    }

    internal fun receivedResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MooncloakVpnService.RequestCode.PREPARE) {
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

                mutableConnection.value = connection
            }
        }
    }

    internal companion object {

        private const val TAG: String = "VPNConnectionManager"
    }
}
