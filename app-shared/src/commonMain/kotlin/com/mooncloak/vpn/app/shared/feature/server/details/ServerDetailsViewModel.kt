package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionManager
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Stable
@FeatureScoped
public class ServerDetailsViewModel @Inject public constructor(
    private val serverConnectionManager: ServerConnectionManager
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

        emit(value = state.current.value.copy(server = server))

        // TODO
    }
}
