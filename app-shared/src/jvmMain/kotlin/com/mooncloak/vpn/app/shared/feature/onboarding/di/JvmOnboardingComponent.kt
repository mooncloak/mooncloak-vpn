package com.mooncloak.vpn.app.shared.feature.onboarding.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@FeatureScoped
internal abstract class JvmOnboardingComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : OnboardingComponent()

internal actual fun FeatureDependencies.Companion.createOnboardingComponent(
    applicationDependencies: ApplicationComponent
): OnboardingComponent = JvmOnboardingComponent::class.create(
    applicationDependencies = applicationDependencies
)
