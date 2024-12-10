package com.mooncloak.vpn.app.shared.feature.support

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.ComponentScoped

@Stable
@ComponentScoped
public class SupportViewModel @Inject public constructor(

) : ViewModel<SupportStateModel>(initialStateValue = SupportStateModel()) {

    public fun load() {
        // TODO
    }
}
