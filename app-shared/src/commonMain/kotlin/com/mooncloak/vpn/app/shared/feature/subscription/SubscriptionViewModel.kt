package com.mooncloak.vpn.app.shared.feature.subscription

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.FeatureScoped

@Stable
@FeatureScoped
public class SubscriptionViewModel @Inject public constructor(

) : ViewModel<SubscriptionStateModel>(initialStateValue = SubscriptionStateModel()) {

    public fun load() {
        // TODO:
    }
}
