package com.mooncloak.vpn.app.shared.feature.speedtest

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel

@Stable
public class SpeedTestViewModel @Inject public constructor() :
    ViewModel<SpeedTestStateModel>(
        initialStateValue = SpeedTestStateModel()
    ) {

    public fun load() {

    }
}
