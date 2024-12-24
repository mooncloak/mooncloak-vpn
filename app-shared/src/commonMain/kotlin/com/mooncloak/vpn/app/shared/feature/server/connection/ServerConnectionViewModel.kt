package com.mooncloak.vpn.app.shared.feature.server.connection

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.getOrNull
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionManager
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionRecordRepository
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

@Stable
@FeatureScoped
public class ServerConnectionViewModel @Inject public constructor(
    private val serverConnectionRecordRepository: ServerConnectionRecordRepository,
    private val serverConnectionManager: ServerConnectionManager,
    private val api: MooncloakVpnServiceHttpApi
) : ViewModel<ServerConnectionStateModel>(initialStateValue = ServerConnectionStateModel()) {

    private var connectionJob: Job? = null

    public fun load() {
        connectionJob?.cancel()
        connectionJob = serverConnectionManager.connection
            .onEach { connection ->
                emit(
                    value = state.current.value.copy(
                        connection = connection
                    )
                )
            }
            .launchIn(coroutineScope)

        emit(value = state.current.value.copy(isLoading = true))

        coroutineScope.launch {
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
                emit(
                    value = state.current.value.copy(
                        isLoading = false,
                        errorMessage = getString(Res.string.global_unexpected_error)
                    )
                )
            }
        }
    }

    @OptIn(ExperimentalPaginationAPI::class)
    private suspend fun getServer(): Server? {
        serverConnectionRecordRepository.getLastConnected()?.server?.let { return it }

        serverConnectionRecordRepository.getStarredPage().firstOrNull()?.server?.let { return it }

        return api.paginateServers().getOrNull()?.firstOrNull()
    }
}
