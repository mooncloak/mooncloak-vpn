package com.mooncloak.vpn.app.shared.feature.home

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel

@Stable
public class HomeViewModel @Inject public constructor(

) : ViewModel<HomeStateModel>(initialStateValue = HomeStateModel()) {

    public fun load() {
        // TODO
    }
}
