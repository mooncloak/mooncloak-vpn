package com.mooncloak.vpn.app.shared.feature.app

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.update
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.storage.AppStorage
import com.mooncloak.vpn.app.shared.storage.PreferencesStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@OptIn(ExperimentalPersistentStateAPI::class)
@Stable
@FeatureScoped
public class ApplicationRootViewModel @Inject public constructor(
    private val appStorage: AppStorage,
    private val preferencesStorage: PreferencesStorage,
    private val navController: NavController
) : ViewModel<ApplicationRootStateModel>(initialStateValue = ApplicationRootStateModel()) {

    private val mutex = Mutex(locked = false)

    public fun load() {
        emit(value = state.current.value.copy(isLoading = true))

        coroutineScope.launch {
            mutex.withLock {
                val viewedOnboarding = appStorage.viewedOnboarding.current.value
                val alwaysDisplayLanding = preferencesStorage.alwaysDisplayLanding.current.value

                val destination = if (viewedOnboarding && !alwaysDisplayLanding) {
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
