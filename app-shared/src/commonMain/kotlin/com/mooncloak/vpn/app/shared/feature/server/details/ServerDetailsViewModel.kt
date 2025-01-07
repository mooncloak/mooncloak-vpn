package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkInfo
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkManager
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionRecord
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionRecordRepository
import com.mooncloak.vpn.app.shared.api.server.getOrNull
import com.mooncloak.vpn.app.shared.api.vpn.isDisconnected
import com.mooncloak.vpn.app.shared.di.FeatureScoped
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
public class ServerDetailsViewModel @Inject public constructor(
    private val serverConnectionManager: VPNConnectionManager,
    private val serverConnectionRecordRepository: ServerConnectionRecordRepository,
    private val localNetworkManager: LocalNetworkManager
) : ViewModel<ServerDetailsStateModel>(initialStateValue = ServerDetailsStateModel()) {

    private val mutex = Mutex(locked = false)

    private var connectionJob: Job? = null

    public fun load(server: Server) {
        connectionJob?.cancel()
        connectionJob = serverConnectionManager.connection
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

        coroutineScope.launch {
            emit(
                value = state.current.value.copy(
                    isLoading = true,
                    server = server
                )
            )

            var record: ServerConnectionRecord? = null
            var localNetworkInfo: LocalNetworkInfo? = null

            try {
                record = serverConnectionRecordRepository.getOrNull(id = server.id)
                localNetworkInfo = localNetworkManager.getInfo()

                emit(
                    value = state.current.value.copy(
                        isLoading = false,
                        server = server,
                        lastConnected = record?.lastConnected,
                        localNetworkInfo = localNetworkInfo
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
                        localNetworkInfo = localNetworkInfo
                    )
                )
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
                        serverConnectionManager.connect(server)
                    } else {
                        serverConnectionManager.disconnect()
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
