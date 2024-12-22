package com.mooncloak.vpn.app.shared.feature.subscription.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.subscription.SubscriptionViewModel

@FeatureScoped
internal abstract class SubscriptionComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: SubscriptionViewModel
}

internal expect fun FeatureDependencies.Companion.createSubscriptionComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): SubscriptionComponent
