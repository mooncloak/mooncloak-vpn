package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkInfo
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkManager
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionManager
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionRecord
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionRecordRepository
import com.mooncloak.vpn.app.shared.api.server.getOrNull
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
public class ServerDetailsViewModel @Inject public constructor(
    private val serverConnectionManager: ServerConnectionManager,
    private val serverConnectionRecordRepository: ServerConnectionRecordRepository,
    private val localNetworkManager: LocalNetworkManager
) : ViewModel<ServerDetailsStateModel>(initialStateValue = ServerDetailsStateModel()) {

    private var connectionJob: Job? = null

    public fun load(server: Server) {
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
}
