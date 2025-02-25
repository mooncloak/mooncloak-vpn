package com.mooncloak.vpn.app.shared.api.billing.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.api.shared.service.isActive
import com.mooncloak.vpn.app.shared.api.service.ServiceTokensRepository
import com.mooncloak.vpn.app.shared.settings.SubscriptionSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock

public class GetServiceSubscriptionFlowUseCase @Inject public constructor(
    private val serviceTokensRepository: ServiceTokensRepository,
    private val subscriptionStorage: SubscriptionSettings,
    private val clock: Clock,
    private val api: MooncloakVpnServiceHttpApi
) {

    public operator fun invoke(): Flow<ServiceSubscription?> =
        getCurrentFlow()
    // FIXME: .onCompletion { emitAll(subscriptionStorage.subscription.flow) }

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
