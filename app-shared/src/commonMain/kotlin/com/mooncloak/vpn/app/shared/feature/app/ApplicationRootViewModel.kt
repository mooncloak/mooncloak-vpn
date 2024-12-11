package com.mooncloak.vpn.app.shared.feature.app

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.storage.AppStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Stable
@ComponentScoped
public class ApplicationRootViewModel @Inject public constructor(
    private val appStorage: AppStorage,
    private val navController: NavController
) : ViewModel<ApplicationRootStateModel>(initialStateValue = ApplicationRootStateModel()) {

    private val mutex = Mutex(locked = false)

    @OptIn(ExperimentalPersistentStateAPI::class)
    public fun load() {
        coroutineScope.launch {
            mutex.withLock {
                val viewedOnboarding = appStorage.viewedOnboarding.current.value

                if (viewedOnboarding) {
                    navController.navigate(RootDestination.Main)
                } else {
                    navController.navigate(RootDestination.Onboarding)
                }
            }
        }
    }
}
