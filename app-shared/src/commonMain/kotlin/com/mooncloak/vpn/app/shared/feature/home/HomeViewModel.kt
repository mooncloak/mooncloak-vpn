package com.mooncloak.vpn.app.shared.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.api.location.Country
import com.mooncloak.vpn.app.shared.api.location.CountryCode
import com.mooncloak.vpn.app.shared.api.location.invoke
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkInfo
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkManager
import com.mooncloak.vpn.app.shared.api.server.ConnectionType
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionManager
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscription
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.feature.home.model.HomeFeedItem
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_accounts
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_data_creeps
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_data_selling
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_subscriptions
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_tracking
import com.mooncloak.vpn.app.shared.resource.onboarding_description_payment_crypto
import com.mooncloak.vpn.app.shared.resource.onboarding_description_payment_google_play
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_accounts
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_data_creeps
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_data_selling
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_subscriptions
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_tracking
import com.mooncloak.vpn.app.shared.resource.onboarding_title_payment_crypto
import com.mooncloak.vpn.app.shared.resource.onboarding_title_payment_google_play
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalPersistentStateAPI::class)
@Stable
@FeatureScoped
public class HomeViewModel @Inject public constructor(
    appClientInfo: AppClientInfo,
    private val subscriptionStorage: SubscriptionStorage,
    private val serverConnectionManager: ServerConnectionManager,
    private val localNetworkManager: LocalNetworkManager
) : ViewModel<HomeStateModel>(initialStateValue = HomeStateModel()) {

    private val noSubscriptionItems = listOf(
        HomeFeedItem.ServerConnectionItem(
            country = Country(
                code = CountryCode(value = "us"),
                name = "US"
            ),
            server = Server(
                id = "1",
                name = "Default Server",
                countryCode = CountryCode(value = "us")
            ),
            connectionType = ConnectionType.MultipleVpn,
            connected = false
        ),
        HomeFeedItem.GetVPNServiceItem,
        HomeFeedItem.ShowcaseItem(
            icon = { rememberVectorPainter(Icons.Default.CloudOff) },
            title = { stringResource(Res.string.onboarding_title_no_tracking) },
            description = { stringResource(Res.string.onboarding_description_no_tracking) }
        ),
        HomeFeedItem.ShowcaseItem(
            icon = { rememberVectorPainter(Icons.Default.PersonOff) },
            title = { stringResource(Res.string.onboarding_title_no_accounts) },
            description = { stringResource(Res.string.onboarding_description_no_accounts) }
        ),
        HomeFeedItem.ShowcaseItem(
            icon = { rememberVectorPainter(Icons.Default.VisibilityOff) },
            title = { stringResource(Res.string.onboarding_title_no_data_creeps) },
            description = { stringResource(Res.string.onboarding_description_no_data_creeps) }
        ),
        HomeFeedItem.ShowcaseItem(
            icon = { rememberVectorPainter(Icons.Default.MoneyOff) },
            title = { stringResource(Res.string.onboarding_title_no_data_selling) },
            description = { stringResource(Res.string.onboarding_description_no_data_selling) }
        ),
        HomeFeedItem.ShowcaseItem(
            icon = { rememberVectorPainter(Icons.Default.Subscriptions) },
            title = { stringResource(Res.string.onboarding_title_no_subscriptions) },
            description = { stringResource(Res.string.onboarding_description_no_subscriptions) }
        ),
        if (appClientInfo.isGooglePlayBuild) {
            HomeFeedItem.ShowcaseItem(
                icon = { rememberVectorPainter(Icons.Default.PlayArrow) },
                title = { stringResource(Res.string.onboarding_title_payment_google_play) },
                description = { stringResource(Res.string.onboarding_description_payment_google_play) }
            )
        } else {
            HomeFeedItem.ShowcaseItem(
                icon = { rememberVectorPainter(Icons.Default.CurrencyBitcoin) },
                title = { stringResource(Res.string.onboarding_title_payment_crypto) },
                description = { stringResource(Res.string.onboarding_description_payment_crypto) }
            )
        }
    )

    private var connectionJob: Job? = null

    public fun load() {
        coroutineScope.launch {
            emit(value = state.current.value.copy(isLoading = true))

            connectionJob?.cancel()
            connectionJob = serverConnectionManager.connection
                .onEach { connection ->
                    emit(
                        value = state.current.value.copy(
                            connectionStatus = connection.status
                        )
                    )
                }
                .catch { e -> LogPile.error(message = "Error listening to connection changes.", cause = e) }
                .launchIn(coroutineScope)

            var subscription: ServiceSubscription? = null
            var localNetworkInfo: LocalNetworkInfo? = null

            try {
                subscription = subscriptionStorage.subscription.current.value
                localNetworkInfo = localNetworkManager.getInfo()

                // TODO: If the subscription model is null, but we have the tokens, load the updated subscription model from the cloud API.

                val items = if (subscription == null) {
                    noSubscriptionItems
                } else {
                    // TODO: The list of items for the subscribed user.
                    emptyList<HomeFeedItem>()
                }

                println("load: items: $items")

                emit(
                    value = state.current.value.copy(
                        subscription = subscription,
                        localNetwork = localNetworkInfo,
                        items = items,
                        isLoading = false
                    )
                )
            } catch (e: Exception) {
                LogPile.error(message = "Error loading home state.", cause = e)

                emit(
                    value = state.current.value.copy(
                        subscription = subscription,
                        localNetwork = localNetworkInfo,
                        isLoading = false,
                        errorMessage = getString(Res.string.global_unexpected_error)
                    )
                )
            }
        }
    }
}
