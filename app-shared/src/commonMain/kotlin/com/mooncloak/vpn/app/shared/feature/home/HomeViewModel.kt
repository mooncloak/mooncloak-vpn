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
import com.mooncloak.vpn.app.shared.api.network.DeviceIPAddressProvider
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkInfo
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkManager
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnection
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscription
import com.mooncloak.vpn.app.shared.api.vpn.connectedTo
import com.mooncloak.vpn.app.shared.api.vpn.isConnected
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalPersistentStateAPI::class)
@Stable
@FeatureScoped
public class HomeViewModel @Inject public constructor(
    appClientInfo: AppClientInfo,
    private val subscriptionStorage: SubscriptionStorage,
    private val serverConnectionManager: VPNConnectionManager,
    private val localNetworkManager: LocalNetworkManager,
    private val deviceIPAddressProvider: DeviceIPAddressProvider
) : ViewModel<HomeStateModel>(initialStateValue = HomeStateModel()) {

    private val showcaseItems = listOf(
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

    private val mutex = Mutex(locked = false)

    private var connectionJob: Job? = null
    private var subscriptionJob: Job? = null

    public fun load() {
        coroutineScope.launch {
            emit(value = state.current.value.copy(isLoading = true))

            connectionJob?.cancel()
            connectionJob = serverConnectionManager.connection
                .onEach { connection ->
                    emit { current ->
                        val updatedItems = getFeedItems(
                            hasSubscription = current.subscription != null,
                            connection = connection
                        )

                        current.copy(
                            connection = connection,
                            items = updatedItems,
                            isCheckingStatus = false
                        )
                    }
                }
                .catch { e -> LogPile.error(message = "Error listening to connection changes.", cause = e) }
                .flowOn(Dispatchers.Main)
                .launchIn(coroutineScope)

            subscriptionJob?.cancel()
            subscriptionJob = flowOf(subscriptionStorage.subscription.flow.value)
                .onCompletion { emitAll(subscriptionStorage.subscription.flow) }
                .onEach { subscription ->
                    emit { current ->
                        val updatedItems = getFeedItems(
                            hasSubscription = subscription != null,
                            connection = current.connection
                        )

                        current.copy(
                            subscription = subscription,
                            items = updatedItems
                        )
                    }
                }
                .catch { e -> LogPile.error(message = "Error listening to subscription changes.", cause = e) }
                .flowOn(Dispatchers.Main)
                .launchIn(coroutineScope)

            var subscription: ServiceSubscription? = null
            var localNetworkInfo: LocalNetworkInfo? = null
            var deviceIpAddress: String? = null

            try {
                subscription = subscriptionStorage.subscription.current.value
                localNetworkInfo = localNetworkManager.getInfo()
                deviceIpAddress = deviceIPAddressProvider.get()

                // TODO: If the subscription model is null, but we have the tokens, load the updated subscription model from the cloud API.

                val items = getFeedItems(
                    hasSubscription = subscription != null,
                    connection = state.current.value.connection
                )

                emit { current ->
                    current.copy(
                        subscription = subscription,
                        localNetwork = localNetworkInfo,
                        deviceIpAddress = deviceIpAddress,
                        items = items,
                        isLoading = false,
                        isCheckingStatus = false
                    )
                }
            } catch (e: Exception) {
                LogPile.error(message = "Error loading home state.", cause = e)

                emit { current ->
                    current.copy(
                        subscription = subscription,
                        localNetwork = localNetworkInfo,
                        deviceIpAddress = deviceIpAddress,
                        isLoading = false,
                        isCheckingStatus = false,
                        errorMessage = getString(Res.string.global_unexpected_error)
                    )
                }
            }
        }
    }

    public fun toggleConnection(server: Server) {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    val currentConnection = state.current.value.connection

                    if (currentConnection is VPNConnection.Connected && currentConnection.connectedTo(server)) {
                        serverConnectionManager.disconnect()
                    } else {
                        serverConnectionManager.connect(server)
                    }
                } catch (e: Exception) {
                    LogPile.error(message = "Error connecting to VPN server.", cause = e)

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

    private fun getFeedItems(
        hasSubscription: Boolean,
        connection: VPNConnection
    ): List<HomeFeedItem> {
        val connectionItem = if (connection.isConnected()) {
            HomeFeedItem.ServerConnectionItem(
                connection = connection
            )
        } else {
            null
        }

        val firstItems = if (hasSubscription || connection.isConnected()) {
            emptyList<HomeFeedItem>()
        } else {
            listOf(HomeFeedItem.GetVPNServiceItem)
        }

        // TODO: The list of items for the subscribed user.
        return if (connectionItem != null) {
            firstItems + connectionItem + showcaseItems
        } else {
            firstItems + showcaseItems
        }
    }
}
