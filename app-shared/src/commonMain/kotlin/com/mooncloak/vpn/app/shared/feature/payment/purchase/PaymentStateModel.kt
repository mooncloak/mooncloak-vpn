package com.mooncloak.vpn.app.shared.feature.payment.purchase

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import com.mooncloak.vpn.api.shared.billing.BitcoinPlanInvoice
import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.api.shared.billing.PlanPaymentStatus
import com.mooncloak.vpn.app.shared.feature.payment.purchase.model.PaymentDestination
import com.mooncloak.vpn.app.shared.theme.ThemePreference

@Immutable
public data class PaymentStateModel public constructor(
    public val screenTitle: String? = null,
    public val destination: PaymentDestination? = null,
    public val selectedPlan: Plan? = null,
    public val plans: List<Plan> = emptyList(),
    public val acceptedTerms: Boolean = false,
    public val termsAndConditionsText: @Composable () -> AnnotatedString = { AnnotatedString("") },
    public val noticeText: String? = null,
    public val invoice: BitcoinPlanInvoice? = null,
    public val paymentStatus: PlanPaymentStatus? = null,
    public val isLoading: Boolean = true,
    public val isPurchasing: Boolean = false,
    public val themePreference: ThemePreference = ThemePreference.System,
    public val errorMessage: String? = null
)
