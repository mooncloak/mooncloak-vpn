package com.mooncloak.vpn.app.shared.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionStatus
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.feature.home.model.HomeFeedItem
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.onboarding_description_crypto_payment
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_accounts
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_data_creeps
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_subscriptions
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_tracking
import com.mooncloak.vpn.app.shared.resource.onboarding_title_crypto_payment
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_accounts
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_data_creeps
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_subscriptions
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_tracking
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalPersistentStateAPI::class)
@Stable
@FeatureScoped
public class HomeViewModel @Inject public constructor(
    private val subscriptionStorage: SubscriptionStorage
) : ViewModel<HomeStateModel>(initialStateValue = HomeStateModel()) {

    private val noSubscriptionItems = listOf(
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
            icon = { rememberVectorPainter(Icons.Default.Subscriptions) },
            title = { stringResource(Res.string.onboarding_title_no_subscriptions) },
            description = { stringResource(Res.string.onboarding_description_no_subscriptions) }
        ),
        HomeFeedItem.ShowcaseItem(
            icon = { rememberVectorPainter(Icons.Default.CurrencyBitcoin) },
            title = { stringResource(Res.string.onboarding_title_crypto_payment) },
            description = { stringResource(Res.string.onboarding_description_crypto_payment) }
        )
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
