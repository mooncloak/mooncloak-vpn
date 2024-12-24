package com.mooncloak.vpn.app.shared.feature.payment.history

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.api.billing.ServicePurchaseReceiptRepository
import com.mooncloak.vpn.app.shared.di.FeatureScoped

@Stable
@FeatureScoped
public class PaymentHistoryViewModel @Inject public constructor(
    private val servicePurchaseReceiptRepository: ServicePurchaseReceiptRepository
) : ViewModel<PaymentHistoryStateModel>(initialStateValue = PaymentHistoryStateModel()) {

    public fun load() {
        // TODO:
    }
}
