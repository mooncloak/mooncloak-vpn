package com.mooncloak.vpn.app.shared.feature.app

import androidx.compose.runtime.Stable
import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.update
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.settings.AppSettings
import com.mooncloak.vpn.app.shared.settings.UserPreferenceSettings
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlin.coroutines.resume

@OptIn(ExperimentalPersistentStateAPI::class)
@Stable
@FeatureScoped
public class ApplicationRootViewModel @Inject public constructor(
    private val appStorage: AppSettings,
    private val preferencesStorage: UserPreferenceSettings,
    private val navController: NavController,
    private val systemAuthenticationProvider: SystemAuthenticationProvider,
    private val clock: Clock
) : ViewModel<ApplicationRootStateModel>(initialStateValue = ApplicationRootStateModel()) {

    private val mutex = Mutex(locked = false)

    public fun load() {
        emit(value = state.current.value.copy(isLoading = true))

        coroutineScope.launch {
            mutex.withLock {
                val viewedOnboarding = appStorage.viewedOnboarding.current.value
                val alwaysDisplayLanding = preferencesStorage.alwaysDisplayLanding.current.value
                val requireAuth = systemAuthenticationProvider.shouldLaunch()

                val destination = when {
                    requireAuth -> RootDestination.SystemAuth
                    viewedOnboarding && !alwaysDisplayLanding -> RootDestination.Main
                    else -> RootDestination.Onboarding
                }

                emit(
                    value = state.current.value.copy(
                        startDestination = destination,
                        isLoading = false
                    )
                )

                if (requireAuth) {
                    val authenticated = suspendCancellableCoroutine { continuation ->
                        systemAuthenticationProvider.launchAuthentication(
                            title = "Authentication Required", // TODO: Fix hardcoded string resource
                            onError = { code, message ->
                                LogPile.error(message = "Error authenticating. $code: $message")

                                continuation.resume(false)
                            },
                            onFailed = {
                                continuation.resume(false)
                            },
                            onSuccess = {
                                continuation.resume(true)
                            }
                        )
                    }

                    if (authenticated) {
                        appStorage.lastAuthenticated.update { clock.now() }

                        val nextDestination = when {
                            viewedOnboarding && !alwaysDisplayLanding -> RootDestination.Main
                            else -> RootDestination.Onboarding
                        }

                        navController.navigate(nextDestination) {
                            popUpTo(RootDestination.SystemAuth) {
                                inclusive = true
                            }
                        }
                    }
                }
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
