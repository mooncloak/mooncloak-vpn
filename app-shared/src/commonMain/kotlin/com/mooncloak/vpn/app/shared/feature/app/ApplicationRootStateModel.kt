package com.mooncloak.vpn.app.shared.feature.app

import androidx.compose.runtime.Immutable

@Immutable
public data class ApplicationRootStateModel public constructor(
    public val startDestination: RootDestination? = null,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
