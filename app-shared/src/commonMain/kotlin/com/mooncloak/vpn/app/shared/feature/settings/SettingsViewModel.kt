package com.mooncloak.vpn.app.shared.feature.settings

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscriptionFlowProvider
import com.mooncloak.vpn.api.shared.network.ip.DeviceIPAddressProvider
import com.mooncloak.vpn.api.shared.network.ip.LocalDeviceIPAddressProvider
import com.mooncloak.vpn.api.shared.preference.WireGuardPreferences
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.api.shared.service.isActive
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsAppDetails
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsDeviceDetails
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.app_copyright
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.resource.subscription_no_active_plan
import com.mooncloak.vpn.app.shared.resource.subscription_title_active_plan
import com.mooncloak.vpn.app.shared.settings.UserPreferenceSettings
import com.mooncloak.vpn.app.shared.theme.ThemePreference
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.getString
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Stable
@FeatureScoped
public class SettingsViewModel @Inject public constructor(
    private val appClientInfo: AppClientInfo,
    private val preferencesStorage: UserPreferenceSettings,
    private val systemAuthenticationProvider: SystemAuthenticationProvider,
    private val clock: Clock,
    private val localDeviceIPAddressProvider: LocalDeviceIPAddressProvider,
    private val deviceIPAddressProvider: DeviceIPAddressProvider,
    private val getServiceSubscriptionFlow: ServiceSubscriptionFlowProvider
) : ViewModel<SettingsStateModel>(initialStateValue = SettingsStateModel()) {

    private val mutex = Mutex(locked = false)

    private var subscriptionJob: Job? = null

    public fun load() {
        coroutineScope.launch {
            emit(value = state.current.value.copy(isLoading = true))

            var appDetails: SettingsAppDetails? = state.current.value.appDetails
            var deviceDetails: SettingsDeviceDetails? = state.current.value.deviceDetails
            var wireGuardPreferences: WireGuardPreferences? = state.current.value.wireGuardPreferences
            var aboutUri: String? = state.current.value.aboutUri
            var privacyPolicyUri: String? = state.current.value.privacyPolicyUri
            var termsUri: String? = state.current.value.termsUri
            var sourceCodeUri: String? = state.current.value.sourceCodeUri
            var startOnLandingScreen = state.current.value.startOnLandingScreen
            var copyright: String? = state.current.value.copyright
            var isSystemAuthSupported = state.current.value.isSystemAuthSupported
            var requireSystemAuth = state.current.value.requireSystemAuth
            var systemAuthTimeout = state.current.value.systemAuthTimeout
            var themePreference = state.current.value.themePreference

            try {
                appDetails = SettingsAppDetails(
                    id = appClientInfo.id,
                    name = appClientInfo.name,
                    version = appClientInfo.versionName,
                    isDebug = appClientInfo.isDebug,
                    isPreRelease = appClientInfo.isPreRelease,
                    buildTime = appClientInfo.buildTime
                )
                deviceDetails = getDeviceDetails()
                wireGuardPreferences = preferencesStorage.wireGuard.get()
                aboutUri = appClientInfo.aboutUri
                privacyPolicyUri = appClientInfo.privacyPolicyUri
                termsUri = appClientInfo.termsAndConditionsUri
                sourceCodeUri = appClientInfo.sourceCodeUri
                startOnLandingScreen = preferencesStorage.alwaysDisplayLanding.get() ?: false
                copyright = getString(
                    Res.string.app_copyright,
                    clock.now().toLocalDateTime(TimeZone.currentSystemDefault()).year.toString()
                )
                isSystemAuthSupported = systemAuthenticationProvider.isSupported
                requireSystemAuth = preferencesStorage.requireSystemAuth.get() ?: false
                systemAuthTimeout = preferencesStorage.systemAuthTimeout.get() ?: 5.minutes
                themePreference = preferencesStorage.theme.get() ?: ThemePreference.System

                emit { current ->
                    current.copy(
                        isLoading = false,
                        appDetails = appDetails,
                        deviceDetails = deviceDetails,
                        wireGuardPreferences = wireGuardPreferences,
                        aboutUri = aboutUri,
                        privacyPolicyUri = privacyPolicyUri,
                        termsUri = termsUri,
                        sourceCodeUri = sourceCodeUri,
                        startOnLandingScreen = startOnLandingScreen,
                        copyright = copyright,
                        isSystemAuthSupported = isSystemAuthSupported,
                        requireSystemAuth = requireSystemAuth,
                        systemAuthTimeout = systemAuthTimeout,
                        themePreference = themePreference
                    )
                }
            } catch (e: Exception) {
                emit { current ->
                    current.copy(
                        isLoading = false,
                        errorMessage = e.message ?: getString(Res.string.global_unexpected_error),
                        appDetails = appDetails,
                        deviceDetails = deviceDetails,
                        wireGuardPreferences = wireGuardPreferences,
                        aboutUri = aboutUri,
                        privacyPolicyUri = privacyPolicyUri,
                        termsUri = termsUri,
                        sourceCodeUri = sourceCodeUri,
                        startOnLandingScreen = startOnLandingScreen,
                        copyright = copyright,
                        isSystemAuthSupported = isSystemAuthSupported,
                        requireSystemAuth = requireSystemAuth,
                        systemAuthTimeout = systemAuthTimeout,
                        themePreference = themePreference
                    )
                }
            }
        }

        subscriptionJob?.cancel()
        subscriptionJob = getServiceSubscriptionFlow()
            .onEach { subscription ->
                val planText = getPlanText(subscription)

                emit { current ->
                    current.copy(
                        currentPlan = planText
                    )
                }
            }
            .catch { e -> LogPile.error(message = "Error listening to subscription changes.", cause = e) }
            .flowOn(Dispatchers.Main)
            .launchIn(coroutineScope)
    }

    public fun toggleStartOnLandingScreen(checked: Boolean) {
        coroutineScope.launch {
            mutex.withLock {
                emit { current ->
                    current.copy(
                        startOnLandingScreen = checked
                    )
                }

                try {
                    preferencesStorage.alwaysDisplayLanding.set(checked)
                } catch (e: Exception) {
                    LogPile.error(message = "Error storing 'start on landing screen' toggle value.", cause = e)

                    emit { current ->
                        current.copy(
                            startOnLandingScreen = !checked,
                            errorMessage = getString(Res.string.global_unexpected_error)
                        )
                    }
                }
            }
        }
    }

    public fun toggleRequireSystemAuth(checked: Boolean) {
        coroutineScope.launch {
            mutex.withLock {
                emit { current ->
                    current.copy(
                        requireSystemAuth = checked
                    )
                }

                try {
                    preferencesStorage.requireSystemAuth.set(checked)
                } catch (e: Exception) {
                    LogPile.error(message = "Error storing 'require system auth' toggle value.", cause = e)

                    emit { current ->
                        current.copy(
                            requireSystemAuth = !checked,
                            errorMessage = getString(Res.string.global_unexpected_error)
                        )
                    }
                }
            }
        }
    }

    public fun updateSystemAuthTimeout(timeout: Duration) {
        coroutineScope.launch {
            mutex.withLock {
                val systemAuthTimeout = state.current.value.systemAuthTimeout

                emit { current ->
                    current.copy(
                        systemAuthTimeout = timeout
                    )
                }

                try {
                    preferencesStorage.systemAuthTimeout.set(timeout)
                } catch (e: Exception) {
                    LogPile.error(message = "Error storing 'system auth timeout' value.", cause = e)

                    emit { current ->
                        current.copy(
                            systemAuthTimeout = systemAuthTimeout,
                            errorMessage = getString(Res.string.global_unexpected_error)
                        )
                    }
                }
            }
        }
    }

    public fun updateThemePreference(value: ThemePreference) {
        coroutineScope.launch {
            mutex.withLock {
                val currentTheme = state.current.value.themePreference

                emit { current ->
                    current.copy(
                        themePreference = value
                    )
                }

                try {
                    preferencesStorage.theme.set(value)
                } catch (e: Exception) {
                    LogPile.error(message = "Error storing 'theme preference' value.", cause = e)

                    emit { current ->
                        current.copy(
                            themePreference = currentTheme,
                            errorMessage = getString(Res.string.global_unexpected_error)
                        )
                    }
                }
            }
        }
    }

    private suspend fun getDeviceDetails(): SettingsDeviceDetails =
        try {
            val publicIp = deviceIPAddressProvider.get()
            val localIp = localDeviceIPAddressProvider.get()

            SettingsDeviceDetails(
                publicIpAddress = publicIp,
                localIpAddress = localIp
            )
        } catch (e: Exception) {
            // Don't fail on loading the device information because it is just a nice to have and shouldn't crash the
            // app if we fail to load it.
            LogPile.error(
                message = "Failed to load device details on the Settings Screen.",
                cause = e
            )

            SettingsDeviceDetails()
        }

    private suspend fun getPlanText(subscription: ServiceSubscription?): String =
        when {
            subscription == null -> getString(Res.string.subscription_no_active_plan)
            !subscription.isActive() -> getString(Res.string.subscription_no_active_plan)
            else -> getString(Res.string.subscription_title_active_plan)
        }
}
