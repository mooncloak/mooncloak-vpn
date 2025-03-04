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
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscriptionFlowProvider
import com.mooncloak.vpn.api.shared.network.DeviceIPAddressProvider
import com.mooncloak.vpn.api.shared.network.LocalNetworkInfo
import com.mooncloak.vpn.api.shared.network.LocalNetworkManager
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.ServerConnectionRecordRepository
import com.mooncloak.vpn.api.shared.vpn.VPNConnection
import com.mooncloak.vpn.api.shared.vpn.VPNConnectionManager
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.api.shared.vpn.connectedTo
import com.mooncloak.vpn.api.shared.vpn.isConnected
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.feature.home.model.HomeFeedItem
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.resource.home_feed_item_server_last_connected
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
import com.mooncloak.vpn.app.shared.settings.SubscriptionSettings
import com.mooncloak.vpn.app.shared.util.time.remaining
import com.mooncloak.vpn.util.shared.time.DurationFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.days

@Stable
@FeatureScoped
public class HomeViewModel @Inject public constructor(
    appClientInfo: AppClientInfo,
    private val serviceConnectionRecordRepository: ServerConnectionRecordRepository,
    private val subscriptionStorage: SubscriptionSettings,
    private val serverConnectionManager: VPNConnectionManager,
    private val localNetworkManager: LocalNetworkManager,
    private val deviceIPAddressProvider: DeviceIPAddressProvider,
    private val getServiceSubscriptionFlow: ServiceSubscriptionFlowProvider,
    private val clock: Clock,
    private val durationFormatter: DurationFormatter = DurationFormatter.remaining()
) : ViewModel<HomeStateModel>(initialStateValue = HomeStateModel()) {

    private val showcaseItems = listOf(
        HomeFeedItem.ShowcaseItem(
            id = "ShowcaseItemNoTracking",
            icon = { rememberVectorPainter(Icons.Default.CloudOff) },
            title = { stringResource(Res.string.onboarding_title_no_tracking) },
            description = { stringResource(Res.string.onboarding_description_no_tracking) }
        ),
        HomeFeedItem.ShowcaseItem(
            id = "ShowcaseItemNoAccounts",
            icon = { rememberVectorPainter(Icons.Default.PersonOff) },
            title = { stringResource(Res.string.onboarding_title_no_accounts) },
            description = { stringResource(Res.string.onboarding_description_no_accounts) }
        ),
        HomeFeedItem.ShowcaseItem(
            id = "ShowcaseItemNoDataCreeps",
            icon = { rememberVectorPainter(Icons.Default.VisibilityOff) },
            title = { stringResource(Res.string.onboarding_title_no_data_creeps) },
            description = { stringResource(Res.string.onboarding_description_no_data_creeps) }
        ),
        HomeFeedItem.ShowcaseItem(
            id = "ShowcaseItemNoDataSelling",
            icon = { rememberVectorPainter(Icons.Default.MoneyOff) },
            title = { stringResource(Res.string.onboarding_title_no_data_selling) },
            description = { stringResource(Res.string.onboarding_description_no_data_selling) }
        ),
        HomeFeedItem.ShowcaseItem(
            id = "ShowcaseItemNoSubscriptions",
            icon = { rememberVectorPainter(Icons.Default.Subscriptions) },
            title = { stringResource(Res.string.onboarding_title_no_subscriptions) },
            description = { stringResource(Res.string.onboarding_description_no_subscriptions) }
        ),
        if (appClientInfo.isGooglePlayBuild) {
            HomeFeedItem.ShowcaseItem(
                id = "ShowcaseItemGooglePlayBilling",
                icon = { rememberVectorPainter(Icons.Default.PlayArrow) },
                title = { stringResource(Res.string.onboarding_title_payment_google_play) },
                description = { stringResource(Res.string.onboarding_description_payment_google_play) }
            )
        } else {
            HomeFeedItem.ShowcaseItem(
                id = "ShowcaseItemCrypto",
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

            var initialSubscription: ServiceSubscription? = null
            var localNetworkInfo: LocalNetworkInfo? = null
            var deviceIpAddress: String? = null

            try {
                initialSubscription = subscriptionStorage.subscription.get()
                localNetworkInfo = localNetworkManager.getInfo()
                deviceIpAddress = deviceIPAddressProvider.get()

                val items = getFeedItems(
                    subscription = initialSubscription,
                    connection = state.current.value.connection
                )

                emit { current ->
                    current.copy(
                        subscription = initialSubscription,
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
                        subscription = initialSubscription,
                        localNetwork = localNetworkInfo,
                        deviceIpAddress = deviceIpAddress,
                        isLoading = false,
                        isCheckingStatus = false,
                        errorMessage = getString(Res.string.global_unexpected_error)
                    )
                }
            }

            connectionJob?.cancel()
            connectionJob = serverConnectionManager.connection
                .onEach { connection ->
                    emit { current ->
                        val updatedItems = getFeedItems(
                            subscription = current.subscription,
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
            subscriptionJob = getServiceSubscriptionFlow()
                .onEach { subscription ->
                    emit { current ->
                        val updatedItems = getFeedItems(
                            subscription = subscription,
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

    private suspend fun getFeedItems(
        subscription: ServiceSubscription?,
        connection: VPNConnection
    ): List<HomeFeedItem> {
        val items = mutableListOf<HomeFeedItem>()

        val now = clock.now()

        if (subscription == null && !connection.isConnected()) {
            items.add(HomeFeedItem.GetVPNServiceItem)
        }

        if (connection.isConnected()) {
            items.add(HomeFeedItem.ServerConnectionItem(connection = connection))
        }

        if (!connection.isConnected()) {
            serviceConnectionRecordRepository.getLastConnected()?.let { record ->
                items.add(
                    HomeFeedItem.ServerItem(
                        server = record.server,
                        connected = false,
                        label = getString(Res.string.home_feed_item_server_last_connected)
                    )
                )
            }
        }

        if (subscription != null && subscription.expiration > now) {
            val remaining = subscription.expiration - now

            items.add(
                HomeFeedItem.PlanUsageItem(
                    durationRemaining = durationFormatter.format(remaining),
                    bytesRemaining = null,
                    showBoost = remaining < 5.days
                )
            )
        }

        items.addAll(showcaseItems)

        // TODO: The list of items for the subscribed user.
        return items
    }
}
