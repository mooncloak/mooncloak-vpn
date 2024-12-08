package com.mooncloak.vpn.app.shared.feature.settings

import androidx.compose.runtime.Immutable

@Immutable
public data class SettingsStateModel public constructor(
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
