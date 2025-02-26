package com.mooncloak.vpn.app.shared.api.billing

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.vpn.api.shared.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.api.shared.service.isActive
import com.mooncloak.vpn.api.shared.service.ServiceTokensRepository
import com.mooncloak.vpn.app.shared.settings.SubscriptionSettings
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.shareIn
import kotlinx.datetime.Clock

@Singleton
public class ServiceSubscriptionFlowProvider @Inject public constructor(
    private val serviceTokensRepository: ServiceTokensRepository,
    private val subscriptionStorage: SubscriptionSettings,
    private val clock: Clock,
    private val api: MooncloakVpnServiceHttpApi,
    applicationCoroutineScope: ApplicationCoroutineScope
) {

    private val subscriptionFlow = getCurrentFlow()
        .onCompletion { emitAll(subscriptionStorage.subscription.flow()) }
        .shareIn(
            scope = applicationCoroutineScope,
            started = SharingStarted.Lazily,
            replay = 1
        )

    public fun get(): Flow<ServiceSubscription?> = subscriptionFlow

    public operator fun invoke(): Flow<ServiceSubscription?> = get()

    private fun getCurrentFlow(): Flow<ServiceSubscription?> = flow {
        var subscription = subscriptionStorage.subscription.get()

        emit(subscription)

        val tokens = serviceTokensRepository.getLatest()

        if ((subscription == null || !subscription.isActive(clock.now())) && tokens != null) {
            try {
                subscription = api.getCurrentSubscription(token = tokens.accessToken)

                subscriptionStorage.subscription.set(subscription)

                emit(subscription)
            } catch (e: Exception) {
                LogPile.error(
                    message = "Error retrieving subscription",
                    cause = e
                )
            }
        }
    }
}
