package com.mooncloak.vpn.app.shared.feature.server.list

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.billing.usecase.GetServiceSubscriptionFlowUseCase
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.storage.SubscriptionSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

@Stable
@FeatureScoped
public class ServerListViewModel @Inject public constructor(
    private val api: MooncloakVpnServiceHttpApi,
    private val subscriptionStorage: SubscriptionSettings,
    private val serverConnectionManager: VPNConnectionManager,
    private val appClientInfo: AppClientInfo,
    private val getServiceSubscriptionFlow: GetServiceSubscriptionFlowUseCase
) : ViewModel<ServerListStateModel>(initialStateValue = ServerListStateModel()) {

    private var connectionJob: Job? = null
    private var subscriptionJob: Job? = null

    @OptIn(ExperimentalPersistentStateAPI::class, ExperimentalPaginationAPI::class)
    public fun load() {
        coroutineScope.launch {
            emit(value = state.current.value.copy(isLoading = true))

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

            subscriptionJob?.cancel()
            subscriptionJob = getServiceSubscriptionFlow()
                .onEach { subscription -> emit { current -> current.copy(subscription = subscription) } }
                .catch { e -> LogPile.error(message = "Error listening to subscription changes.", cause = e) }
                .flowOn(Dispatchers.Main)
                .launchIn(coroutineScope)

            try {
                val token = subscriptionStorage.tokens.get()?.accessToken

                // TODO: Properly support paginating servers
                val servers = api.paginateServers(
                    token = token
                )

                emit { current ->
                    current.copy(
                        isLoading = false,
                        servers = servers.items,
                        isPreRelease = appClientInfo.isPreRelease
                    )
                }
            } catch (e: Exception) {
                LogPile.error(message = "Error loading servers.", cause = e)

                emit { current ->
                    current.copy(
                        isLoading = false,
                        errorMessage = getString(Res.string.global_unexpected_error)
                    )
                }
            }
        }
    }
}
