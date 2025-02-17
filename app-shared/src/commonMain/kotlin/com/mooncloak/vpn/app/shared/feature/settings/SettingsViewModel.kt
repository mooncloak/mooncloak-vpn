package com.mooncloak.vpn.app.shared.feature.settings

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.logpile.core.info
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.update
import com.mooncloak.vpn.app.shared.api.network.DeviceIPAddressProvider
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkManager
import com.mooncloak.vpn.app.shared.api.preference.WireGuardPreferences
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsAppDetails
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsDeviceDetails
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.app_copyright
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.resource.subscription_no_active_plan
import com.mooncloak.vpn.app.shared.storage.PreferencesStorage
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.getString
import kotlin.time.Duration

@OptIn(ExperimentalPersistentStateAPI::class)
@Stable
@FeatureScoped
public class SettingsViewModel @Inject public constructor(
    private val appClientInfo: AppClientInfo,
    private val subscriptionStorage: SubscriptionStorage,
    private val preferencesStorage: PreferencesStorage,
    private val systemAuthenticationProvider: SystemAuthenticationProvider,
    private val clock: Clock,
    private val localNetworkManager: LocalNetworkManager,
    private val deviceIPAddressProvider: DeviceIPAddressProvider,
) : ViewModel<SettingsStateModel>(initialStateValue = SettingsStateModel()) {

    @OptIn(ExperimentalPersistentStateAPI::class)
    public fun load() {
        LogPile.info("SettingsViewModel: load")
        coroutineScope.launch {
            emit(value = state.current.value.copy(isLoading = true))

            var appDetails: SettingsAppDetails? = state.current.value.appDetails
            var deviceDetails: SettingsDeviceDetails? = state.current.value.deviceDetails
            var wireGuardPreferences: WireGuardPreferences? = state.current.value.wireGuardPreferences
            var privacyPolicyUri: String? = state.current.value.privacyPolicyUri
            var termsUri: String? = state.current.value.termsUri
            var sourceCodeUri: String? = state.current.value.sourceCodeUri
            var currentPlan: String? = state.current.value.currentPlan
            var startOnLandingScreen = state.current.value.startOnLandingScreen
            var copyright: String? = state.current.value.copyright
            var isSystemAuthSupported = state.current.value.isSystemAuthSupported
            var requireSystemAuth = state.current.value.requireSystemAuth
            var systemAuthTimeout = state.current.value.systemAuthTimeout

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
                wireGuardPreferences = preferencesStorage.wireGuard.current.value
                privacyPolicyUri = appClientInfo.privacyPolicyUri
                termsUri = appClientInfo.termsAndConditionsUri
                sourceCodeUri = appClientInfo.sourceCodeUri
                startOnLandingScreen = preferencesStorage.alwaysDisplayLanding.current.value
                copyright = getString(
                    Res.string.app_copyright,
                    clock.now().toLocalDateTime(TimeZone.currentSystemDefault()).year.toString()
                )

                val subscription = subscriptionStorage.subscription.current.value

                currentPlan = when (subscription) {
                    null -> getString(Res.string.subscription_no_active_plan)
                    else -> "Active plan" // TODO: Format Remaining data and time
                }

                isSystemAuthSupported = systemAuthenticationProvider.isSupported
                requireSystemAuth = preferencesStorage.requireSystemAuth.current.value
                systemAuthTimeout = preferencesStorage.systemAuthTimeout.current.value

                emit(
                    value = state.current.value.copy(
                        isLoading = false,
                        appDetails = appDetails,
                        deviceDetails = deviceDetails,
                        wireGuardPreferences = wireGuardPreferences,
                        currentPlan = currentPlan,
                        privacyPolicyUri = privacyPolicyUri,
                        termsUri = termsUri,
                        sourceCodeUri = sourceCodeUri,
                        startOnLandingScreen = startOnLandingScreen,
                        copyright = copyright,
                        isSystemAuthSupported = isSystemAuthSupported,
                        requireSystemAuth = requireSystemAuth,
                        systemAuthTimeout = systemAuthTimeout
                    )
                )
            } catch (e: Exception) {
                emit(
                    value = state.current.value.copy(
                        isLoading = false,
                        errorMessage = e.message ?: getString(Res.string.global_unexpected_error),
                        appDetails = appDetails,
                        deviceDetails = deviceDetails,
                        wireGuardPreferences = wireGuardPreferences,
                        currentPlan = currentPlan,
                        privacyPolicyUri = privacyPolicyUri,
                        termsUri = termsUri,
                        sourceCodeUri = sourceCodeUri,
                        startOnLandingScreen = startOnLandingScreen,
                        copyright = copyright,
                        isSystemAuthSupported = isSystemAuthSupported,
                        requireSystemAuth = requireSystemAuth,
                        systemAuthTimeout = systemAuthTimeout
                    )
                )
            }
        }
    }

    public fun toggleStartOnLandingScreen(checked: Boolean) {
        coroutineScope.launch {
            emit(
                value = state.current.value.copy(
                    startOnLandingScreen = checked
                )
            )

            try {
                preferencesStorage.alwaysDisplayLanding.update(checked)
            } catch (e: Exception) {
                LogPile.error(message = "Error storing 'start on landing screen' toggle value.", cause = e)

                emit(
                    value = state.current.value.copy(
                        startOnLandingScreen = !checked,
                        errorMessage = getString(Res.string.global_unexpected_error)
                    )
                )
            }
        }
    }

    public fun toggleRequireSystemAuth(checked: Boolean) {
        coroutineScope.launch {
            emit(
                value = state.current.value.copy(
                    requireSystemAuth = checked
                )
            )

            try {
                preferencesStorage.requireSystemAuth.update(checked)
            } catch (e: Exception) {
                LogPile.error(message = "Error storing 'require system auth' toggle value.", cause = e)

                emit(
                    value = state.current.value.copy(
                        requireSystemAuth = !checked,
                        errorMessage = getString(Res.string.global_unexpected_error)
                    )
                )
            }
        }
    }

    public fun updateSystemAuthTimeout(timeout: Duration) {
        coroutineScope.launch {
            val current = state.current.value.systemAuthTimeout

            emit(
                value = state.current.value.copy(
                    systemAuthTimeout = timeout
                )
            )

            try {
                preferencesStorage.systemAuthTimeout.update(timeout)
            } catch (e: Exception) {
                LogPile.error(message = "Error storing 'system auth timeout' value.", cause = e)

                emit(
                    value = state.current.value.copy(
                        systemAuthTimeout = current,
                        errorMessage = getString(Res.string.global_unexpected_error)
                    )
                )
            }
        }
    }

    private suspend fun getDeviceDetails(): SettingsDeviceDetails =
        try {
            val publicIp = deviceIPAddressProvider.get()
            val localIp = localNetworkManager.getInfo()?.ipAddress

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
}
