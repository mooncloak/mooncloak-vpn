package com.mooncloak.vpn.app.shared.feature.onboarding

import androidx.compose.runtime.Immutable

@Immutable
public data class OnboardingStateModel public constructor(
    public val startDestination: OnboardingDestination = OnboardingDestination.Landing,
    public val appVersion: String? = null,
    public val viewedOnboarding: Boolean = false,
    public val isGooglePlayBuild: Boolean = false,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
