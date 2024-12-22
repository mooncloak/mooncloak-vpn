package com.mooncloak.vpn.app.shared.feature.main

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.feature.app.MainDestination
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Stable
@FeatureScoped
public class MainViewModel @Inject public constructor(
    private val navController: NavController
) : ViewModel<MainStateModel>(initialStateValue = MainStateModel()) {

    private val mutex = Mutex(locked = false)

    public fun init(startDestination: MainDestination) {
        coroutineScope.launch {
            mutex.withLock {
                emit(
                    value = state.current.value.copy(
                        startDestination = startDestination,
                        destinationStates = state.current.value.destinationStates.map { destinationState ->
                            destinationState.copy(
                                selected = destinationState.destination == startDestination
                            )
                        }.distinctBy { it.destination }
                            .toSet()
                    ))
            }
        }
    }

    public fun select(destination: MainDestination) {
        coroutineScope.launch {
            mutex.withLock {
                navController.navigate(
                    route = destination,
                    navOptions = NavOptions.Builder()
                        .setLaunchSingleTop(true)
                        .setRestoreState(true)
                        // This only sets a single destination in the backstack.
                        // This way the back function is handled appropriately.
                        // Adapted from the following documentation:
                        // https://developer.android.com/develop/ui/compose/navigation#bottom-nav
                        .setPopUpTo(
                            destinationId = navController.graph.findStartDestination().id,
                            inclusive = true,
                            saveState = true
                        )
                        .build()
                )

                emit(
                    value = state.current.value.copy(
                        destinationStates = state.current.value.destinationStates.map { destinationState ->
                            destinationState.copy(
                                selected = destinationState.destination == destination
                            )
                        }.distinctBy { it.destination }
                            .toSet()
                    ))
            }
        }
    }
}
