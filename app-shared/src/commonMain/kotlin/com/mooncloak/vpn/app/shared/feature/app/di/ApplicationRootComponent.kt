package com.mooncloak.vpn.app.shared.feature.app.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.app.ApplicationRootViewModel

@ComponentScoped
internal abstract class ApplicationRootComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: ApplicationRootViewModel
}

internal expect fun FeatureDependencies.Companion.createApplicationRootComponent(
    applicationDependencies: ApplicationComponent
): ApplicationRootComponent
