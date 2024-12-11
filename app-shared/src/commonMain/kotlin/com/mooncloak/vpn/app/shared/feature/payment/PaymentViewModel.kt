package com.mooncloak.vpn.app.shared.feature.payment

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.ComponentScoped

@Stable
@ComponentScoped
public class PaymentViewModel @Inject public constructor(

) : ViewModel<PaymentStateModel>(initialStateValue = PaymentStateModel()) {

    public fun load() {
        // TODO:
    }
}
