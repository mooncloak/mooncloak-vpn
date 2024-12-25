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
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.api.location.Country
import com.mooncloak.vpn.app.shared.api.location.CountryCode
import com.mooncloak.vpn.app.shared.api.server.ConnectionType
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionStatus
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.feature.home.model.HomeFeedItem
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.resource.Res
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalPersistentStateAPI::class)
@Stable
@FeatureScoped
public class HomeViewModel @Inject public constructor(
    private val appClientInfo: AppClientInfo,
    private val subscriptionStorage: SubscriptionStorage
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

    public fun load() {
        coroutineScope.launch {
            emit(value = state.current.value.copy(isLoading = true))

            val subscription = subscriptionStorage.subscription.current.value

            // TODO: If the subscription model is null, but we have the tokens, load the updated subscription model from the cloud API.

            val items = if (subscription == null) {
                noSubscriptionItems
            } else {
                // TODO: The list of items for the subscribed user.
                emptyList<HomeFeedItem>()
            }

            emit(
                value = state.current.value.copy(
                    subscription = subscription,
                    items = items,
                    isLoading = false
                )
            )

            delay(2.seconds)

            emit(value = state.current.value.copy(connection = ServerConnectionStatus.Disconnected))

            // TODO
        }
    }
}
