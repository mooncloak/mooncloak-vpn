package com.mooncloak.vpn.app.shared.feature.home.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.home.HomeViewModel

@ComponentScoped
internal abstract class HomeComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: HomeViewModel
}

internal expect fun FeatureDependencies.Companion.createHomeComponent(
    applicationDependencies: ApplicationComponent
): HomeComponent
