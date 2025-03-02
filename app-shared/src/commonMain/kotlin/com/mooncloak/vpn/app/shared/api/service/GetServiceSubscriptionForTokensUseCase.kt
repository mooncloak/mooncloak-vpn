package com.mooncloak.vpn.app.shared.api.service

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.api.shared.service.ServiceTokens
import com.mooncloak.vpn.app.shared.settings.SubscriptionSettings

public class GetServiceSubscriptionForTokensUseCase @Inject public constructor(
    private val api: VpnServiceApi,
    private val subscriptionStorage: SubscriptionSettings
) {

    public suspend operator fun invoke(tokens: ServiceTokens): ServiceSubscription {
        val subscription = api.getCurrentSubscription(token = tokens.accessToken)

        subscriptionStorage.subscription.set(subscription)

        return subscription
    }
}
