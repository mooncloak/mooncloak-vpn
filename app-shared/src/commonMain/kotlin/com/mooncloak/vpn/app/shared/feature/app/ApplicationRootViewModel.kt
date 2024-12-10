package com.mooncloak.vpn.app.shared.feature.app

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration.Companion.seconds

@Stable
@ComponentScoped
public class ApplicationRootViewModel @Inject public constructor() :
    ViewModel<ApplicationRootStateModel>(initialStateValue = ApplicationRootStateModel()) {

    private val mutex = Mutex(locked = false)

    public fun load() {
        coroutineScope.launch {
            mutex.withLock {
                delay(1.seconds)

                emit(value = state.current.value.copy(startDestination = RootDestination.Main))
            }
        }
    }
}
