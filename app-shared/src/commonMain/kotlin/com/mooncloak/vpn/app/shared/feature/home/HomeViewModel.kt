package com.mooncloak.vpn.app.shared.feature.home

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.api.ServerConnectionStatus
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Stable
@ComponentScoped
public class HomeViewModel @Inject public constructor(

) : ViewModel<HomeStateModel>(initialStateValue = HomeStateModel()) {

    public fun load() {
        coroutineScope.launch {
            delay(2.seconds)

            emit(value = state.current.value.copy(connection = ServerConnectionStatus.Disconnected))

            // TODO
        }
    }
}
