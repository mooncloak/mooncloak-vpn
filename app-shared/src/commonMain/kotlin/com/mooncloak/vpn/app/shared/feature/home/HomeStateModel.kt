package com.mooncloak.vpn.app.shared.feature.home

import androidx.compose.runtime.Immutable

@Immutable
public data class HomeStateModel public constructor(
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
