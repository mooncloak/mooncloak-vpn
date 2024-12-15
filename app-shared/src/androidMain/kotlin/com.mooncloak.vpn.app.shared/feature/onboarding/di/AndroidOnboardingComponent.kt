package com.mooncloak.vpn.app.shared.feature.onboarding.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class AndroidOnboardingComponent internal constructor(
    @Component internal val presentationDependencies: PresentationComponent
) : OnboardingComponent()

internal actual fun FeatureDependencies.Companion.createOnboardingComponent(
    presentationDependencies: PresentationComponent
): OnboardingComponent = AndroidOnboardingComponent::class.create(
    presentationDependencies = presentationDependencies
)
