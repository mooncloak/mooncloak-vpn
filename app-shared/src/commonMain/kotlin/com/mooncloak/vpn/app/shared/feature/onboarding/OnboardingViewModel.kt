package com.mooncloak.vpn.app.shared.feature.onboarding

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.di.ComponentScoped

@Stable
@ComponentScoped
public class OnboardingViewModel @Inject public constructor(

) : ViewModel<OnboardingStateModel>(initialStateValue = OnboardingStateModel()) {


}
