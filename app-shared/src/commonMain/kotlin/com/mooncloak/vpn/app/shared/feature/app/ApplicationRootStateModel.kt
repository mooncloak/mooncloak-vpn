package com.mooncloak.vpn.app.shared.feature.app

import androidx.compose.runtime.Immutable

@Immutable
public data class ApplicationRootStateModel public constructor(
    public val startDestination: RootDestination = RootDestination.Splash,
    public val errorMessage: String? = null
)
