package com.mooncloak.vpn.app.shared.feature.subscription.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@FeatureScoped
internal abstract class AndroidSubscriptionComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : SubscriptionComponent()

internal actual fun FeatureDependencies.Companion.createSubscriptionComponent(
    applicationDependencies: ApplicationComponent
): SubscriptionComponent = AndroidSubscriptionComponent::class.create(
    applicationDependencies = applicationDependencies
)
