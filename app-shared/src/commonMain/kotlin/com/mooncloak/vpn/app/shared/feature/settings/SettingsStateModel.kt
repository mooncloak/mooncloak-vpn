package com.mooncloak.vpn.app.shared.feature.settings

import androidx.compose.runtime.Immutable

@Immutable
public data class SettingsStateModel public constructor(
    public val privacyPolicyUri: String? = null,
    public val termsUri: String? = null,
    public val sourceCodeUri: String? = null,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
