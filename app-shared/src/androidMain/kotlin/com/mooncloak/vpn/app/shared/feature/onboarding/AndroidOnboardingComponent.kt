package com.mooncloak.vpn.app.shared.feature.onboarding

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createOnboardingComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): OnboardingComponent = OnboardingComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
