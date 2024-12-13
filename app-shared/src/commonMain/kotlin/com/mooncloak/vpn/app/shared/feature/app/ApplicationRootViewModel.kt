package com.mooncloak.vpn.app.shared.feature.app

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.update
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.storage.AppStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@OptIn(ExperimentalPersistentStateAPI::class)
@Stable
@ComponentScoped
public class ApplicationRootViewModel @Inject public constructor(
    private val appStorage: AppStorage,
    private val navController: NavController
) : ViewModel<ApplicationRootStateModel>(initialStateValue = ApplicationRootStateModel()) {

    private val mutex = Mutex(locked = false)

    public fun load() {
        emit(value = state.current.value.copy(isLoading = true))

        coroutineScope.launch {
            mutex.withLock {
                val viewedOnboarding = appStorage.viewedOnboarding.current.value

                val destination = if (viewedOnboarding) {
                    RootDestination.Main
                } else {
                    RootDestination.Onboarding
                }

                emit(
                    value = state.current.value.copy(
                        startDestination = destination,
                        isLoading = false
                    )
                )
            }
        }
    }

    public fun finishOnboarding() {
        coroutineScope.launch {
            mutex.withLock {
                appStorage.viewedOnboarding.update(true)

                navController.navigate(RootDestination.Main) {
                    popUpTo(RootDestination.Onboarding) {
                        inclusive = true
                    }
                }
            }
        }
    }
}
