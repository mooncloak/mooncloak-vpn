package com.mooncloak.vpn.app.shared.app

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Stable
public class ApplicationViewModel @Inject public constructor() :
    ViewModel<ApplicationStateModel>(initialStateValue = ApplicationStateModel()) {

    private val mutex = Mutex(locked = false)

    public fun updateDestination(destination: AppDestination) {
        coroutineScope.launch {
            mutex.withLock {
                /*emit(
                    value = state.current.value.copy(
                        destinationStates = state.current.value.destinationStates.map {
                            it.copy(
                                isSelected = it.destination == destination
                            )
                        }.distinctBy { it.destination }
                            .toSet()
                    )
                )*/
            }
        }
    }
}
