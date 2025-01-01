package com.mooncloak.vpn.app.shared.feature.server.connection

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.getOrNull
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionManager
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionRecordRepository
import com.mooncloak.vpn.app.shared.api.server.isConnected
import com.mooncloak.vpn.app.shared.api.server.isDisconnected
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
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getString

@Stable
@FeatureScoped
public class ServerConnectionViewModel @Inject public constructor(
    private val serverConnectionRecordRepository: ServerConnectionRecordRepository,
    private val serverConnectionManager: ServerConnectionManager,
    private val api: MooncloakVpnServiceHttpApi
) : ViewModel<ServerConnectionStateModel>(initialStateValue = ServerConnectionStateModel()) {

    private var connectionJob: Job? = null

    private val mutex = Mutex(locked = false)

    public fun load() {
        val current = serverConnectionManager.connection.value

        connectionJob?.cancel()
        connectionJob = serverConnectionManager.connection
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
    }

    public fun connect() {
        coroutineScope.launch {
            mutex.withLock {
                emit(value = state.current.value.copy(isLoading = true))

                try {
                    val server = getServer()

                    emit(
                        value = state.current.value.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    )

                    if (server != null) {
                        serverConnectionManager.connect(server)
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

    public fun disconnect() {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    serverConnectionManager.disconnect()
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

    @OptIn(ExperimentalPaginationAPI::class)
    private suspend fun getServer(): Server? {
        serverConnectionRecordRepository.getLastConnected()?.server?.let { return it }

        serverConnectionRecordRepository.getStarredPage().firstOrNull()?.server?.let { return it }

        return withContext(Dispatchers.IO) {
            api.paginateServers().getOrNull()?.firstOrNull()
        }
    }
}
