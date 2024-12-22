package com.mooncloak.vpn.app.shared.feature.server.connection

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.FeatureScoped

@Stable
@FeatureScoped
public class ServerConnectionViewModel @Inject public constructor(

) : ViewModel<ServerConnectionStateModel>(initialStateValue = ServerConnectionStateModel()) {

    public fun load() {
        // TODO
    }
}
