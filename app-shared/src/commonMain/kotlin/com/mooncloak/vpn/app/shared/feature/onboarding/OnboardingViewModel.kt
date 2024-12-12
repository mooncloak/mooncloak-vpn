package com.mooncloak.vpn.app.shared.feature.onboarding

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.feature.app.AppClientInfo

@Stable
@ComponentScoped
public class OnboardingViewModel @Inject public constructor(
    private val appClientInfo: AppClientInfo
) : ViewModel<OnboardingStateModel>(initialStateValue = OnboardingStateModel()) {

    public fun load() {
        emit(
            value = state.current.value.copy(
                appVersion = appClientInfo.versionName
            )
        )
    }
}
