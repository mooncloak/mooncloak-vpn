package com.mooncloak.vpn.app.shared.feature.subscription.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class AndroidSubscriptionComponent internal constructor(
    @Component internal val presentationDependencies: PresentationComponent
) : SubscriptionComponent()

internal actual fun FeatureDependencies.Companion.createSubscriptionComponent(
    presentationDependencies: PresentationComponent
): SubscriptionComponent = AndroidSubscriptionComponent::class.create(
    presentationDependencies = presentationDependencies
)
