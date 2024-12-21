package com.mooncloak.vpn.app.shared.feature.settings

import androidx.compose.runtime.Immutable

@Immutable
public data class SettingsStateModel public constructor(
    public val appVersion: String? = null,
    public val currentPlan: String? = null,
    public val privacyPolicyUri: String? = null,
    public val termsUri: String? = null,
    public val sourceCodeUri: String? = null,
    public val copyright: String? = null,
    public val startOnLandingScreen: Boolean = false,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
