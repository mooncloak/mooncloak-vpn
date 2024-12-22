package com.mooncloak.vpn.app.shared.storage

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.persistence.PersistableStateContainer
import com.mooncloak.kodetools.statex.persistence.persistableStateContainerOf
import com.mooncloak.vpn.app.shared.theme.ThemePreference
import kotlinx.serialization.modules.SerializersModule

@Singleton
@OptIn(ExperimentalPersistentStateAPI::class)
public class PreferencesStorage @Inject public constructor(
    serializersModule: SerializersModule
) {

    public val theme: PersistableStateContainer<ThemePreference?> = persistableStateContainerOf(
        key = THEME,
        defaultValue = null,
        serializersModule = serializersModule
    )

    public val alwaysDisplayLanding: PersistableStateContainer<Boolean> = persistableStateContainerOf(
        key = ALWAYS_DISPLAY_LANDING,
        defaultValue = false,
        serializersModule = serializersModule
    )

    public val requireSystemAuth: PersistableStateContainer<Boolean> = persistableStateContainerOf(
        key = REQUIRE_SYSTEM_AUTH,
        defaultValue = false,
        serializersModule = serializersModule
    )

    internal companion object Key {

        private const val THEME = "com.mooncloak.vpn.app.storage.key.theme"
        private const val ALWAYS_DISPLAY_LANDING = "com.mooncloak.vpn.app.storage.key.landing"
        private const val REQUIRE_SYSTEM_AUTH = "com.mooncloak.vpn.app.storage.key.system_auth"
    }
}
