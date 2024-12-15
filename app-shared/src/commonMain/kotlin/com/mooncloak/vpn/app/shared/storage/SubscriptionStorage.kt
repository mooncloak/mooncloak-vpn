package com.mooncloak.vpn.app.shared.storage

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.persistence.PersistableStateContainer
import com.mooncloak.kodetools.statex.persistence.persistableStateContainerOf
import com.mooncloak.vpn.app.shared.api.ServiceSubscription
import com.mooncloak.vpn.app.shared.api.ServiceTokens
import kotlinx.serialization.modules.SerializersModule

@Singleton
@OptIn(ExperimentalPersistentStateAPI::class)
public class SubscriptionStorage @Inject public constructor(
    serializersModule: SerializersModule
) {

    public val tokens: PersistableStateContainer<ServiceTokens?> = persistableStateContainerOf(
        key = TOKENS,
        defaultValue = null,
        serializersModule = serializersModule
    )

    public val subscription: PersistableStateContainer<ServiceSubscription?> = persistableStateContainerOf(
        key = SUBSCRIPTION,
        defaultValue = null,
        serializersModule = serializersModule
    )

    internal companion object Key {

        private const val TOKENS = "com.mooncloak.vpn.app.storage.key.tokens"
        private const val SUBSCRIPTION = "com.mooncloak.vpn.app.storage.key.subscription"
    }
}
