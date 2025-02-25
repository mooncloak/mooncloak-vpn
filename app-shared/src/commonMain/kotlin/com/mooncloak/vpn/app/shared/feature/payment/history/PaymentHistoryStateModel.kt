package com.mooncloak.vpn.app.shared.feature.payment.history

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.billing.ServicePurchaseReceipt

@Immutable
public data class PaymentHistoryStateModel public constructor(
    public val receipts: List<ServicePurchaseReceipt> = emptyList(),
    public val isLoading: Boolean = true,
    public val errorMessage: String? = null
)
