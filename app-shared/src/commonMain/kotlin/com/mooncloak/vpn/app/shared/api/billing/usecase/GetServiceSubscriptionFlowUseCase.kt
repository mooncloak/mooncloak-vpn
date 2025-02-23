package com.mooncloak.vpn.app.shared.api.billing.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.update
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscription
import com.mooncloak.vpn.app.shared.api.service.isActive
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.datetime.Clock

@OptIn(ExperimentalPersistentStateAPI::class)
public class GetServiceSubscriptionFlowUseCase @Inject public constructor(
    private val subscriptionStorage: SubscriptionStorage,
    private val clock: Clock,
    private val api: MooncloakVpnServiceHttpApi
) {

    public operator fun invoke(): Flow<ServiceSubscription?> =
        getCurrentFlow()
            .onCompletion { emitAll(subscriptionStorage.subscription.flow) }

    private fun getCurrentFlow(): Flow<ServiceSubscription?> = flow {
        var subscription = subscriptionStorage.subscription.current.value

        emit(subscription)

        val tokens = subscriptionStorage.tokens.current.value

        if ((subscription == null || !subscription.isActive(clock.now())) && tokens != null) {
            try {
                subscription = api.getCurrentSubscription(token = tokens.accessToken)

                subscriptionStorage.subscription.update(subscription)

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
