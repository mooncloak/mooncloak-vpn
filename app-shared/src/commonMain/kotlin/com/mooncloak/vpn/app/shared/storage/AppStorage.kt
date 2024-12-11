package com.mooncloak.vpn.app.shared.storage

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.persistence.PersistableStateContainer
import com.mooncloak.kodetools.statex.persistence.persistableStateContainerOf
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalPersistentStateAPI::class)
public class AppStorage @Inject public constructor(
    serializersModule: SerializersModule
) {

    public val viewedOnboarding: PersistableStateContainer<Boolean> = persistableStateContainerOf(
        key = ONBOARDING,
        defaultValue = false,
        serializersModule = serializersModule
    )

    internal companion object Key {

        private const val ONBOARDING = "com.mooncloak.vpn.app.storage.key.onboarding"
    }
}
