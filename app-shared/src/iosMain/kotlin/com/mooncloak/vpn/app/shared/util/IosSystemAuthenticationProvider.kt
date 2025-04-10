package com.mooncloak.vpn.app.shared.util

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.settings.AppSettings
import com.mooncloak.vpn.app.shared.settings.UserPreferenceSettings
import com.mooncloak.vpn.util.shared.coroutine.PresentationCoroutineScope
import kotlinx.cinterop.ExperimentalForeignApi
import platform.LocalAuthentication.*
import kotlinx.coroutines.*
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes

public class IosSystemAuthenticationProvider @Inject public constructor(
    private val preferencesStorage: UserPreferenceSettings,
    private val appStorage: AppSettings,
    private val clock: Clock,
    private val coroutineScope: PresentationCoroutineScope
) : SystemAuthenticationProvider {

    private val context = LAContext()

    @OptIn(ExperimentalForeignApi::class)
    override val isSupported: Boolean
        get() = try {
            context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthenticationWithBiometrics, null)
        } catch (_: Exception) {
            false
        }

    @OptIn(ExperimentalPersistentStateAPI::class)
    override suspend fun shouldLaunch(): Boolean {
        if (!isSupported) return false
        if (preferencesStorage.requireSystemAuth.get() != true) return false

        val lastAuthenticated = appStorage.lastAuthenticated.current.value ?: return true
        val timeout = preferencesStorage.systemAuthTimeout.get() ?: 5.minutes
        val now = clock.now()

        return now > lastAuthenticated + timeout
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun launchAuthentication(
        title: String,
        subtitle: String?,
        description: String?,
        onError: (code: Int, message: String?) -> Unit,
        onFailed: () -> Unit,
        onSuccess: () -> Unit
    ) {
        val canEvaluate = try {
            context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthenticationWithBiometrics, null)
        } catch (e: Exception) {
            onError(-1, e.message)

            return
        }

        if (!canEvaluate) {
            onError(-1, "Biometric authentication not available")
            return
        }

        // Set up the reason string (iOS requires a reason)
        val reason = description ?: subtitle ?: title

        // Launch authentication
        context.evaluatePolicy(
            LAPolicyDeviceOwnerAuthenticationWithBiometrics,
            reason
        ) { success, error ->
            coroutineScope.launch {
                when {
                    success -> onSuccess()
                    error != null -> {
                        val code = error.code

                        if (code == LAErrorAuthenticationFailed) {
                            onFailed()
                        } else {
                            onError(code.toInt(), error.localizedDescription)
                        }
                    }

                    else -> onFailed() // Fallback for unexpected failure
                }
            }
        }
    }
}
