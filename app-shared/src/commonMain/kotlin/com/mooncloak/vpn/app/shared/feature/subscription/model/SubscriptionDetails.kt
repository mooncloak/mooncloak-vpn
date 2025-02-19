package com.mooncloak.vpn.app.shared.feature.subscription.model

import androidx.compose.runtime.Immutable

@Immutable
public data class SubscriptionDetails public constructor(
    public val purchased: String? = null,
    public val expiration: String? = null,
    public val totalData: String? = null,
    public val remainingDuration: String? = null,
    public val remainingData: String? = null
)
