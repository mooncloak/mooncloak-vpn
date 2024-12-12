package com.mooncloak.vpn.app.shared.feature.subscription

import androidx.compose.runtime.Immutable

@Immutable
public data class SubscriptionStateModel public constructor(
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
