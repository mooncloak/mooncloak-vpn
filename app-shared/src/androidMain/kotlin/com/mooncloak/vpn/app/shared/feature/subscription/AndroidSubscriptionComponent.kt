package com.mooncloak.vpn.app.shared.feature.subscription

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createSubscriptionComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): SubscriptionComponent = SubscriptionComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
