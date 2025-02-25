package com.mooncloak.vpn.app.shared.storage

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.api.shared.service.ServiceTokens
import com.mooncloak.vpn.data.shared.KeyValueProperty
import com.mooncloak.vpn.data.shared.MutableKeyValueProperty
import com.mooncloak.vpn.data.shared.MutableKeyValueStorage
import com.mooncloak.vpn.data.shared.property

@Singleton
public class SubscriptionStorage @Inject public constructor(
    storage: MutableKeyValueStorage
) {

    public val tokens: MutableKeyValueProperty<ServiceTokens> by storage.property(key = TOKENS)
    public val subscription: MutableKeyValueProperty<ServiceSubscription> by storage.property(key = SUBSCRIPTION)

    internal companion object Key {

        private const val TOKENS = "com.mooncloak.vpn.app.storage.key.tokens"
        private const val SUBSCRIPTION = "com.mooncloak.vpn.app.storage.key.subscription"
    }
}
