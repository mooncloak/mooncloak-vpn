package com.mooncloak.vpn.app.shared.feature.payment

import androidx.compose.runtime.Immutable

@Immutable
public data class PaymentStateModel public constructor(
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
