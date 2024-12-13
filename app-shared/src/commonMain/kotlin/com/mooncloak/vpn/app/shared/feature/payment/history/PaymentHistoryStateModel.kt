package com.mooncloak.vpn.app.shared.feature.payment.history

import androidx.compose.runtime.Immutable

@Immutable
public data class PaymentHistoryStateModel public constructor(
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
