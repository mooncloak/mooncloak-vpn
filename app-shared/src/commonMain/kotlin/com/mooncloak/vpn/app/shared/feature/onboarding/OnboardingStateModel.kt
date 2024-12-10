package com.mooncloak.vpn.app.shared.feature.onboarding

import androidx.compose.runtime.Immutable

@Immutable
public data class OnboardingStateModel public constructor(
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
