package com.mooncloak.vpn.app.shared.feature.payment.history

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.onboarding.OnboardingComponent

internal actual fun FeatureDependencies.Companion.createOnboardingComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): OnboardingComponent = OnboardingComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
