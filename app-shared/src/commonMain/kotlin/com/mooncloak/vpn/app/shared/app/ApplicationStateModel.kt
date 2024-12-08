package com.mooncloak.vpn.app.shared.app

import androidx.compose.runtime.Immutable

@Immutable
public data class ApplicationStateModel public constructor(
    public val startDestination: AppDestination = AppDestination.Onboarding
)
