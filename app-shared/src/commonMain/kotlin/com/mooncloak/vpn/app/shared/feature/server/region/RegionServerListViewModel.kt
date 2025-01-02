package com.mooncloak.vpn.app.shared.feature.server.region

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.FeatureScoped

@Stable
@FeatureScoped
public class RegionServerListViewModel @Inject public constructor(

) : ViewModel<RegionServerListStateModel>(initialStateValue = RegionServerListStateModel()) {

    public fun load() {
        // TODO
    }
}
