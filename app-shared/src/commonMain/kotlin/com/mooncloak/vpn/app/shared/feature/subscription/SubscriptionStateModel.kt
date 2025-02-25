package com.mooncloak.vpn.app.shared.feature.subscription

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.billing.ServicePurchaseReceipt
import com.mooncloak.vpn.app.shared.api.plan.Plan
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.api.shared.service.ServiceSubscriptionUsage
import com.mooncloak.vpn.app.shared.feature.subscription.model.SubscriptionDetails

@Immutable
public data class SubscriptionStateModel public constructor(
    public val subscription: ServiceSubscription? = null,
    public val details: SubscriptionDetails? = null,
    public val plan: Plan? = null,
    public val usage: ServiceSubscriptionUsage? = null,
    public val lastReceipt: ServicePurchaseReceipt? = null,
    public val isLoading: Boolean = true,
    public val errorMessage: String? = null
)
