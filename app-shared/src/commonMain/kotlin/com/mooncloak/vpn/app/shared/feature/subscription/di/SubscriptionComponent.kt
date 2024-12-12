package com.mooncloak.vpn.app.shared.feature.subscription.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.subscription.SubscriptionViewModel

@ComponentScoped
internal abstract class SubscriptionComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: SubscriptionViewModel
}

internal expect fun FeatureDependencies.Companion.createSubscriptionComponent(
    applicationDependencies: ApplicationComponent
): SubscriptionComponent
