package com.mooncloak.vpn.app.shared.feature.payment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import com.mooncloak.vpn.app.shared.api.BitcoinPlanInvoice
import com.mooncloak.vpn.app.shared.api.Plan
import com.mooncloak.vpn.app.shared.feature.payment.model.PaymentDestination

@Immutable
public data class PaymentStateModel public constructor(
    public val screenTitle: String? = null,
    public val startDestination: PaymentDestination? = null,
    public val selectedPlan: Plan? = null,
    public val plans: List<Plan> = emptyList(),
    public val acceptedTerms: Boolean = false,
    public val termsAndConditionsText: @Composable () -> AnnotatedString = { AnnotatedString("") },
    public val invoice: BitcoinPlanInvoice? = null,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
