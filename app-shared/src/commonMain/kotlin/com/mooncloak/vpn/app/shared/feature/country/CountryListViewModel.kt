package com.mooncloak.vpn.app.shared.feature.country

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel

@Stable
public class CountryListViewModel @Inject public constructor(

) : ViewModel<CountryListStateModel>(initialStateValue = CountryListStateModel()) {

    public fun load() {
        // TODO
    }
}
