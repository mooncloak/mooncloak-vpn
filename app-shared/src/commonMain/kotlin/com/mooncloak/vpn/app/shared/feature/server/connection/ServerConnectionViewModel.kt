package com.mooncloak.vpn.app.shared.feature.server.connection

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.network.core.vpn.VPNConnectionManager
import com.mooncloak.vpn.network.core.vpn.isConnected
import com.mooncloak.vpn.network.core.vpn.isDisconnected
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.api.server.usecase.GetDefaultServerUseCase
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.compose.resources.getString

@Stable
@FeatureScoped
public class ServerConnectionViewModel @Inject public constructor(
    private val vpnConnectionManager: VPNConnectionManager,
    private val getDefaultServer: GetDefaultServerUseCase
) : ViewModel<ServerConnectionStateModel>(initialStateValue = ServerConnectionStateModel()) {

    private var connectionJob: Job? = null

    private val mutex = Mutex(locked = false)

    public fun load(server: Server?) {
        coroutineScope.launch {
            try {
                emit(value = state.current.value.copy(isLoading = true))

                val current = vpnConnectionManager.connection.value
                val connectionServer = server ?: getDefaultServer()

                emit(
                    value = state.current.value.copy(
                        isLoading = false,
                        server = connectionServer,
                        errorMessage = null
                    )
                )

                connectionJob?.cancel()
                connectionJob = vpnConnectionManager.connection
                    .onEach { connection ->
                        emit { current ->
                            current.copy(connection = connection)
                        }
                    }
                    .catch { e -> LogPile.error(message = "Error listening to connection changes.", cause = e) }
                    .flowOn(Dispatchers.Main)
                    .launchIn(coroutineScope)

                if (current.isConnected()) {
                    disconnect()
                } else if (current.isDisconnected()) {
                    connect()
                }
            } catch (e: Exception) {
                LogPile.error(message = "Error loading server for connection screen.", cause = e)

                emit { current ->
                    current.copy(
                        errorMessage = getString(Res.string.global_unexpected_error),
                        isLoading = false
                    )
                }
            }
        }
    }

    public fun connect() {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    val server = state.current.value.server

                    if (server != null) {
                        vpnConnectionManager.connect(server)
                    } else {
                        LogPile.error(message = "No server to connect to.")

                        emit(
                            value = state.current.value.copy(
                                isLoading = false,
                                errorMessage = getString(Res.string.global_unexpected_error)
                            )
                        )
                    }
                } catch (e: Exception) {
                    LogPile.error(message = "Error connecting to VPN server. ${e.message}", cause = e)

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

    public fun disconnect() {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    vpnConnectionManager.disconnect()
                } catch (e: Exception) {
                    LogPile.error(message = "Error disconnecting from VPN server.", cause = e)

                    emit(
                        value = state.current.value.copy(
                            errorMessage = getString(Res.string.global_unexpected_error)
                        )
                    )
                }
            }
        }
    }
}
