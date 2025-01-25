package com.mooncloak.vpn.app.shared.api.billing.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.update
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscription
import com.mooncloak.vpn.app.shared.api.service.ServiceTokens
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPersistentStateAPI::class)
public class GetCurrentSubscriptionUseCase @Inject public constructor(
    private val api: MooncloakVpnServiceHttpApi,
    private val subscriptionStorage: SubscriptionStorage
) {

    public suspend operator fun invoke(tokens: ServiceTokens): ServiceSubscription {
        val subscription = withContext(Dispatchers.IO) {
            api.getCurrentSubscription(token = tokens.accessToken)
        }

        subscriptionStorage.subscription.update(subscription)

        return subscription
    }
}
