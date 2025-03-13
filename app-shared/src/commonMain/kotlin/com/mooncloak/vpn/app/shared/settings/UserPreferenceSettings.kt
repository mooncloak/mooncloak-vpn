package com.mooncloak.vpn.app.shared.settings

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.api.shared.preference.WireGuardPreferences
import com.mooncloak.vpn.app.shared.theme.ThemePreference
import com.mooncloak.vpn.data.shared.keyvalue.MutableKeyValueProperty
import com.mooncloak.vpn.data.shared.keyvalue.MutableKeyValueStorage
import com.mooncloak.vpn.data.shared.keyvalue.property
import kotlin.time.Duration

@Singleton
public class UserPreferenceSettings @Inject public constructor(
    storage: MutableKeyValueStorage
) {

    public val theme: MutableKeyValueProperty<ThemePreference> by storage.property(key = THEME)

    public val alwaysDisplayLanding: MutableKeyValueProperty<Boolean> by storage.property(key = ALWAYS_DISPLAY_LANDING)

    public val requireSystemAuth: MutableKeyValueProperty<Boolean> by storage.property(key = REQUIRE_SYSTEM_AUTH)

    public val systemAuthTimeout: MutableKeyValueProperty<Duration> by storage.property(key = SYSTEM_AUTH_TIMEOUT)

    public val wireGuard: MutableKeyValueProperty<WireGuardPreferences> by storage.property(key = WIRE_GUARD)

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
