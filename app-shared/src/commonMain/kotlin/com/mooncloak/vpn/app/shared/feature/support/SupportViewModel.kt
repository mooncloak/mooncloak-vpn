package com.mooncloak.vpn.app.shared.feature.support

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel

@Stable
public class SupportViewModel @Inject public constructor(

) : ViewModel<SupportStateModel>(initialStateValue = SupportStateModel()) {

    public fun load() {
        // TODO
    }
}
