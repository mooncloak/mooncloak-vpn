package com.mooncloak.vpn.app.shared.storage

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.persistence.PersistableStateContainer
import com.mooncloak.kodetools.statex.persistence.persistableStateContainerOf
import kotlinx.datetime.Instant
import kotlinx.serialization.modules.SerializersModule

@Singleton
@OptIn(ExperimentalPersistentStateAPI::class)
public class AppStorage @Inject public constructor(
    serializersModule: SerializersModule
) {

    public val viewedOnboarding: PersistableStateContainer<Boolean> = persistableStateContainerOf(
        key = ONBOARDING,
        defaultValue = false,
        serializersModule = serializersModule
    )

    public val lastAuthenticated: PersistableStateContainer<Instant?> = persistableStateContainerOf(
        key = LAST_AUTHENTICATED,
        defaultValue = null,
        serializersModule = serializersModule
    )

    internal companion object Key {

        private const val ONBOARDING = "com.mooncloak.vpn.app.storage.key.onboarding"
        private const val LAST_AUTHENTICATED = "com.mooncloak.vpn.app.storage.key.last_authenticated"
    }
}
