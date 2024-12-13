package com.mooncloak.vpn.app.shared.feature.payment

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.Plan

@Immutable
public data class PaymentStateModel public constructor(
    public val selectedPlan: Plan? = null,
    public val plans: List<Plan> = emptyList(),
    public val acceptedTerms: Boolean = false,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
