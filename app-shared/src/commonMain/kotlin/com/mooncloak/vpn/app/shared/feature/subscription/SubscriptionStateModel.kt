package com.mooncloak.vpn.app.shared.feature.subscription

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.Plan
import com.mooncloak.vpn.app.shared.api.ServiceSubscription
import com.mooncloak.vpn.app.shared.api.ServiceSubscriptionUsage

@Immutable
public data class SubscriptionStateModel public constructor(
    public val subscription: ServiceSubscription? = null,
    public val plan: Plan? = null,
    public val usage: ServiceSubscriptionUsage? = null,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
