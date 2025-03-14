package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.api.shared.network.ip.PublicDeviceIPAddressProvider
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.vpn.VPNConnectionManager
import com.mooncloak.vpn.api.shared.server.ServerConnectionRecord
import com.mooncloak.vpn.api.shared.server.ServerConnectionRecordRepository
import com.mooncloak.vpn.api.shared.vpn.VPNConnection
import com.mooncloak.vpn.api.shared.vpn.connectedTo
import com.mooncloak.vpn.api.shared.vpn.isDisconnected
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.data.shared.repository.getOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.getString
import kotlin.time.Duration.Companion.seconds

@Stable
@FeatureScoped
public class ServerDetailsViewModel @Inject public constructor(
    private val vpnConnectionManager: VPNConnectionManager,
    private val serverConnectionRecordRepository: ServerConnectionRecordRepository,
    private val deviceIPAddressProvider: PublicDeviceIPAddressProvider,
    private val clock: Clock
) : ViewModel<ServerDetailsStateModel>(initialStateValue = ServerDetailsStateModel()) {

    private val mutex = Mutex(locked = false)

    private var connectionJob: Job? = null

    public fun load(server: Server?) {
        coroutineScope.launch {
            if (server != null) {
                emit(
                    value = state.current.value.copy(
                        isLoading = true,
                        server = server
                    )
                )

                var record: ServerConnectionRecord? = null
                var deviceIpAddress: String? = null

                try {
                    record = serverConnectionRecordRepository.getOrNull(id = server.id)
                    deviceIpAddress = deviceIPAddressProvider.get()

                    val startConnection = vpnConnectionManager.connection.value
                    val connected = startConnection.connectedTo(server)

                    emit(
                        value = state.current.value.copy(
                            isLoading = false,
                            server = server,
                            lastConnected = record?.lastConnected,
                            deviceIpAddress = deviceIpAddress,
                            connection = startConnection,
                            startConnectionDuration = if (connected && startConnection is VPNConnection.Connected) {
                                clock.now() - startConnection.timestamp
                            } else {
                                0.seconds
                            }
                        )
                    )
                } catch (e: Exception) {
                    LogPile.error(message = "Error loading server details for server '${server.name}'.", cause = e)

                    emit(
                        value = state.current.value.copy(
                            isLoading = false,
                            errorMessage = getString(Res.string.global_unexpected_error),
                            server = server,
                            lastConnected = record?.lastConnected,
                            deviceIpAddress = deviceIpAddress
                        )
                    )
                }

                connectionJob?.cancel()
                connectionJob = vpnConnectionManager.connection
                    .onStart { vpnConnectionManager.connection.value }
                    .onEach { connection ->
                        emit { current ->
                            current.copy(
                                connection = connection
                            )
                        }
                    }
                    .catch { e -> LogPile.error(message = "Error listening to connection changes.", cause = e) }
                    .flowOn(Dispatchers.Main)
                    .launchIn(coroutineScope)
            }
        }
    }

    public fun toggleConnection() {
        coroutineScope.launch {
            mutex.withLock {
                emit(value = state.current.value.copy(isLoading = true))

                try {
                    val server = state.current.value.server

                    if (state.current.value.connection.isDisconnected() && server != null) {
                        vpnConnectionManager.connect(server)
                    } else {
                        vpnConnectionManager.disconnect()

                        if (server != null) {
                            val lastConnected = serverConnectionRecordRepository.getOrNull(id = server.id)

                            emit(
                                value = state.current.value.copy(
                                    lastConnected = lastConnected?.lastConnected
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    LogPile.error(message = "Error connecting to VPN server.", cause = e)

                    emit(
                        value = state.current.value.copy(
                            isLoading = false,
                            errorMessage = getString(Res.string.global_unexpected_error)
                        )
                    )
                }
            }
        }
    }
}
