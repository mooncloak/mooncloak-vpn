package com.mooncloak.vpn.app.shared.feature.onboarding.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.onboarding.OnboardingViewModel

@FeatureScoped
internal abstract class OnboardingComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: OnboardingViewModel
}

internal expect fun FeatureDependencies.Companion.createOnboardingComponent(
    applicationDependencies: ApplicationComponent
): OnboardingComponent
