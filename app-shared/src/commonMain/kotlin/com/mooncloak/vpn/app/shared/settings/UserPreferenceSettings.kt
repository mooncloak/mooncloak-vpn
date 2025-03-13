package com.mooncloak.vpn.app.shared.settings

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.persistence.PersistableStateContainer
import com.mooncloak.kodetools.statex.persistence.persistableStateContainerOf
import com.mooncloak.vpn.api.shared.preference.WireGuardPreferences
import com.mooncloak.vpn.app.shared.theme.ThemePreference
import com.mooncloak.vpn.data.shared.keyvalue.MutableKeyValueProperty
import com.mooncloak.vpn.data.shared.keyvalue.MutableKeyValueStorage
import com.mooncloak.vpn.data.shared.keyvalue.property
import kotlinx.serialization.modules.SerializersModule
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Singleton
@OptIn(ExperimentalPersistentStateAPI::class)
public class UserPreferenceSettings @Inject public constructor(
    serializersModule: SerializersModule,
    storage: MutableKeyValueStorage
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

    public val systemAuthTimeout: PersistableStateContainer<Duration> = persistableStateContainerOf(
        key = SYSTEM_AUTH_TIMEOUT,
        defaultValue = 5.minutes,
        serializersModule = serializersModule
    )

    public val wireGuard: PersistableStateContainer<WireGuardPreferences> = persistableStateContainerOf(
        key = WIRE_GUARD,
        defaultValue = WireGuardPreferences(),
        serializersModule = serializersModule
    )

    public val moonShieldEnabled: MutableKeyValueProperty<Boolean> by storage.property(key = MOON_SHIELD_ENABLED)

    internal companion object Key {

        private const val THEME = "com.mooncloak.vpn.app.storage.key.theme"
        private const val ALWAYS_DISPLAY_LANDING = "com.mooncloak.vpn.app.storage.key.landing"
        private const val REQUIRE_SYSTEM_AUTH = "com.mooncloak.vpn.app.storage.key.system_auth.enabled"
        private const val SYSTEM_AUTH_TIMEOUT = "com.mooncloak.vpn.app.storage.key.system_auth.timeout"
        private const val WIRE_GUARD = "com.mooncloak.vpn.app.storage.key.wireguard"
        private const val MOON_SHIELD_ENABLED = "com.mooncloak.vpn.app.storage.key.moonshield.enabled"
    }
}
