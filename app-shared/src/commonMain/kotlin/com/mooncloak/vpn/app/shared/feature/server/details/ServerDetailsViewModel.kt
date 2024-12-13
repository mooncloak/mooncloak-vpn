package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.ComponentScoped

@Stable
@ComponentScoped
public class ServerDetailsViewModel @Inject public constructor(

) : ViewModel<ServerDetailsStateModel>(initialStateValue = ServerDetailsStateModel()) {

    public fun load() {
        // TODO
    }
}
