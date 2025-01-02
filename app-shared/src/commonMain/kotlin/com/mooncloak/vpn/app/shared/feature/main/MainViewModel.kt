package com.mooncloak.vpn.app.shared.feature.main

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionManager
import com.mooncloak.vpn.app.shared.feature.app.MainDestination
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Stable
@FeatureScoped
public class MainViewModel @Inject public constructor(
    private val navController: NavController,
    private val subscriptionStorage: SubscriptionStorage,
    private val serverConnectionManager: ServerConnectionManager
) : ViewModel<MainStateModel>(initialStateValue = MainStateModel()) {

    private val mutex = Mutex(locked = false)

    private var subscriptionJob: Job? = null
    private var serverConnectionJob: Job? = null

    @OptIn(ExperimentalPersistentStateAPI::class)
    public fun load() {
        subscriptionJob?.cancel()
        subscriptionJob = subscriptionStorage.subscription.flow
            .onEach { subscription -> emit { current -> current.copy(subscription = subscription) } }
            .onStart { emit { current -> current.copy(subscription = subscriptionStorage.subscription.current.value) } }
            .catch { e -> LogPile.error(message = "Error listening to subscription changes.", cause = e) }
            .flowOn(Dispatchers.Main)
            .launchIn(coroutineScope)

        serverConnectionJob?.cancel()
        serverConnectionJob = serverConnectionManager.connection
            .onEach { connection -> emit { current -> current.copy(serverConnection = connection) } }
            .catch { e -> LogPile.error(message = "Error listening to server connection changes.", cause = e) }
            .flowOn(Dispatchers.Main)
            .launchIn(coroutineScope)
    }

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
