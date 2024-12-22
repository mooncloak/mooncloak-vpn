package com.mooncloak.vpn.app.shared.feature.onboarding.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class JvmOnboardingComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
) : OnboardingComponent()

internal actual fun FeatureDependencies.Companion.createOnboardingComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): OnboardingComponent = JvmOnboardingComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
