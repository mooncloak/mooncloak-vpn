package com.mooncloak.vpn.app.shared.feature.settings

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.preference.WireGuardPreferences
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsAppDetails
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsDeviceDetails
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Immutable
public data class SettingsStateModel public constructor(
    public val appDetails: SettingsAppDetails? = null,
    public val deviceDetails: SettingsDeviceDetails? = null,
    public val wireGuardPreferences: WireGuardPreferences? = null,
    public val currentPlan: String? = null,
    public val privacyPolicyUri: String? = null,
    public val termsUri: String? = null,
    public val sourceCodeUri: String? = null,
    public val copyright: String? = null,
    public val startOnLandingScreen: Boolean = false,
    public val isSystemAuthSupported: Boolean = false,
    public val requireSystemAuth: Boolean = false,
    public val systemAuthTimeout: Duration = 5.minutes,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
