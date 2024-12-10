package com.mooncloak.vpn.app.shared.feature.onboarding.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@ComponentScoped
internal abstract class JvmOnboardingComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : OnboardingComponent()

internal actual fun FeatureDependencies.Companion.createOnboardingComponent(
    applicationDependencies: ApplicationComponent
): OnboardingComponent = JvmOnboardingComponent::class.create(
    applicationDependencies = applicationDependencies
)
