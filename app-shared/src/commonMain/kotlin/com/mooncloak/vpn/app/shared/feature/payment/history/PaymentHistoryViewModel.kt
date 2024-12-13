package com.mooncloak.vpn.app.shared.feature.payment.history

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.ComponentScoped

@Stable
@ComponentScoped
public class PaymentHistoryViewModel @Inject public constructor(
) : ViewModel<PaymentHistoryStateModel>(initialStateValue = PaymentHistoryStateModel()) {

    public fun load() {
        // TODO:
    }
}
