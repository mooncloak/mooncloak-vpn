package com.mooncloak.vpn.app.shared.feature.settings

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.feature.app.AppClientInfo
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.resource.subscription_no_active_plan
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

@Stable
@ComponentScoped
public class SettingsViewModel @Inject public constructor(
    private val appClientInfo: AppClientInfo,
    private val subscriptionStorage: SubscriptionStorage
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

            try {
                appVersion = appClientInfo.versionName
                privacyPolicyUri = appClientInfo.privacyPolicyUri
                termsUri = appClientInfo.termsAndConditionsUri
                sourceCodeUri = appClientInfo.sourceCodeUri

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
                        sourceCodeUri = sourceCodeUri
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
                        sourceCodeUri = sourceCodeUri
                    )
                )
            }
        }
    }
}
