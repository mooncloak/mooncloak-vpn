package com.mooncloak.vpn.app.shared.feature.settings

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.feature.country.CountryListStateModel

@Stable
@ComponentScoped
public class SettingsViewModel @Inject public constructor(

) : ViewModel<SettingsStateModel>(initialStateValue = SettingsStateModel()) {

    public fun load() {
        // TODO
    }
}
