package com.mooncloak.vpn.app.shared.feature.main

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscriptionFlowProvider
import com.mooncloak.vpn.network.core.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.api.server.usecase.GetDefaultServerUseCase
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.util.shortcuts.AppShortcutManager
import com.mooncloak.vpn.util.shortcuts.AppShortcutProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.compose.resources.getString

@Stable
@FeatureScoped
public class MainViewModel @Inject public constructor(
    private val navController: NavController,
    private val serverConnectionManager: VPNConnectionManager,
    private val getDefaultServer: GetDefaultServerUseCase,
    private val getServiceSubscriptionFlow: ServiceSubscriptionFlowProvider,
    private val appShortcutManager: AppShortcutManager,
    private val appShortcutProvider: AppShortcutProvider
) : ViewModel<MainStateModel>(initialStateValue = MainStateModel()) {

    private val mutex = Mutex(locked = false)

    private var subscriptionJob: Job? = null
    private var serverConnectionJob: Job? = null

    public fun load() {
        coroutineScope.launch {
            try {
                val defaultServer = getDefaultServer()

                emit(
                    value = state.current.value.copy(
                        defaultServer = defaultServer,
                        errorMessage = null
                    )
                )
            } catch (e: Exception) {
                LogPile.error(message = "Error retrieving default server.", cause = e)

                emit(value = state.current.value.copy(errorMessage = getString(Res.string.global_unexpected_error)))
            }

            subscriptionJob?.cancel()
            subscriptionJob = getServiceSubscriptionFlow()
                .onEach { subscription -> emit { current -> current.copy(subscription = subscription) } }
                .catch { e -> LogPile.error(message = "Error listening to subscription changes.", cause = e) }
                .flowOn(Dispatchers.Main)
                .launchIn(coroutineScope)

            serverConnectionJob?.cancel()
            serverConnectionJob = serverConnectionManager.connection
                .onEach { connection -> emit { current -> current.copy(connection = connection) } }
                .catch { e -> LogPile.error(message = "Error listening to server connection changes.", cause = e) }
                .flowOn(Dispatchers.Main)
                .launchIn(coroutineScope)

            appShortcutManager.set(
                shortcuts = appShortcutProvider.get()
            )
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
            }
        }
    }
}
