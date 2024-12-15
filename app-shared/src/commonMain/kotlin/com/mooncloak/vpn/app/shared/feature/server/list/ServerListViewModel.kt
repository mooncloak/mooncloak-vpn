package com.mooncloak.vpn.app.shared.feature.server.list

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.ComponentScoped

@Stable
@ComponentScoped
public class ServerListViewModel @Inject public constructor(

) : ViewModel<ServerListStateModel>(initialStateValue = ServerListStateModel()) {

    public fun load() {
        // TODO
    }
}
