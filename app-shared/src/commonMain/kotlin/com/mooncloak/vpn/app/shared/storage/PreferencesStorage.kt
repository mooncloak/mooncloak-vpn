package com.mooncloak.vpn.app.shared.storage

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.persistence.PersistableStateContainer
import com.mooncloak.kodetools.statex.persistence.persistableStateContainerOf
import com.mooncloak.vpn.app.shared.theme.ThemePreference
import kotlinx.serialization.modules.SerializersModule

@OptIn(ExperimentalPersistentStateAPI::class)
public class PreferencesStorage @Inject internal constructor(
    serializersModule: SerializersModule
) {

    public val theme: PersistableStateContainer<ThemePreference?> = persistableStateContainerOf(
        key = THEME,
        defaultValue = null,
        serializersModule = serializersModule
    )

    internal companion object Key {

        private const val THEME = "com.mooncloak.vpn.app.storage.key.theme"
    }
}
