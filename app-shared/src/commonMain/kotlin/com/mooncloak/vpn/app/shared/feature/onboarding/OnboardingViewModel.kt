package com.mooncloak.vpn.app.shared.feature.onboarding

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.settings.AppSettings

@OptIn(ExperimentalPersistentStateAPI::class)
@Stable
@FeatureScoped
public class OnboardingViewModel @Inject public constructor(
    private val appClientInfo: AppClientInfo,
    private val appStorage: AppSettings
) : ViewModel<OnboardingStateModel>(initialStateValue = OnboardingStateModel()) {

    public fun load() {
        emit(
            value = state.current.value.copy(
                appVersion = appClientInfo.versionName,
                viewedOnboarding = appStorage.viewedOnboarding.current.value,
                isGooglePlayBuild = appClientInfo.isGooglePlayBuild
            )
        )
    }
}
