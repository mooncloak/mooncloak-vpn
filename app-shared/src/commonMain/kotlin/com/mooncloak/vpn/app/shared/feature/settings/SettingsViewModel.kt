package com.mooncloak.vpn.app.shared.feature.settings

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.update
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.app_copyright
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.resource.subscription_no_active_plan
import com.mooncloak.vpn.app.shared.storage.PreferencesStorage
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.getString

@OptIn(ExperimentalPersistentStateAPI::class)
@Stable
@FeatureScoped
public class SettingsViewModel @Inject public constructor(
    private val appClientInfo: AppClientInfo,
    private val subscriptionStorage: SubscriptionStorage,
    private val preferencesStorage: PreferencesStorage,
    private val clock: Clock
) : ViewModel<SettingsStateModel>(initialStateValue = SettingsStateModel()) {

    @OptIn(ExperimentalPersistentStateAPI::class)
    public fun load() {
        coroutineScope.launch {
            emit(value = state.current.value.copy(isLoading = true))

            var appVersion: String? = null
            var privacyPolicyUri: String? = null
            var termsUri: String? = null
            var sourceCodeUri: String? = null
            var currentPlan: String? = null
            var startOnLandingScreen = false
            var copyright: String? = null

            try {
                appVersion = appClientInfo.versionName
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

                emit(
                    value = state.current.value.copy(
                        isLoading = false,
                        appVersion = appVersion,
                        currentPlan = currentPlan,
                        privacyPolicyUri = privacyPolicyUri,
                        termsUri = termsUri,
                        sourceCodeUri = sourceCodeUri,
                        startOnLandingScreen = startOnLandingScreen,
                        copyright = copyright
                    )
                )
            } catch (e: Exception) {
                emit(
                    value = state.current.value.copy(
                        isLoading = false,
                        errorMessage = e.message ?: getString(Res.string.global_unexpected_error),
                        appVersion = appVersion,
                        currentPlan = currentPlan,
                        privacyPolicyUri = privacyPolicyUri,
                        termsUri = termsUri,
                        sourceCodeUri = sourceCodeUri,
                        startOnLandingScreen = startOnLandingScreen,
                        copyright = copyright
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

                emit(value = state.current.value.copy(errorMessage = getString(Res.string.global_unexpected_error)))
            }
        }
    }
}
