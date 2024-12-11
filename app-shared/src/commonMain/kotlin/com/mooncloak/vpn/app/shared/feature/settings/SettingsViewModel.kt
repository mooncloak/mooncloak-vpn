package com.mooncloak.vpn.app.shared.feature.settings

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.feature.app.AppClientInfo

@Stable
@ComponentScoped
public class SettingsViewModel @Inject public constructor(
    private val appClientInfo: AppClientInfo
) : ViewModel<SettingsStateModel>(initialStateValue = SettingsStateModel()) {

    public fun load() {
        emit(value = state.current.value.copy(isLoading = true))

        emit(
            value = state.current.value.copy(
                isLoading = false,
                privacyPolicyUri = appClientInfo.privacyPolicyUri,
                termsUri = appClientInfo.termsAndConditionsUri,
                sourceCodeUri = appClientInfo.sourceCodeUri
            )
        )

        // TODO
    }
}
