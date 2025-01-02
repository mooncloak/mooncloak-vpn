package com.mooncloak.vpn.app.shared.feature.server.list

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getString

@Stable
@FeatureScoped
public class ServerListViewModel @Inject public constructor(
    private val api: MooncloakVpnServiceHttpApi,
    private val subscriptionStorage: SubscriptionStorage
) : ViewModel<ServerListStateModel>(initialStateValue = ServerListStateModel()) {

    @OptIn(ExperimentalPersistentStateAPI::class, ExperimentalPaginationAPI::class)
    public fun load() {
        coroutineScope.launch {
            emit(value = state.current.value.copy(isLoading = true))

            try {
                val token = subscriptionStorage.tokens.current.value?.accessToken

                // TODO: Properly support paginating servers
                val servers = withContext(Dispatchers.IO) {
                    api.paginateServers(
                        token = token
                    )
                }

                emit(
                    value = state.current.value.copy(
                        isLoading = false,
                        servers = servers.items
                    )
                )
            } catch (e: Exception) {
                LogPile.error(message = "Error loading servers.", cause = e)

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
