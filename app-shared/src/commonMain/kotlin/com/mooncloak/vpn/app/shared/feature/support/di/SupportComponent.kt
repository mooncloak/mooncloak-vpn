package com.mooncloak.vpn.app.shared.feature.support.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.support.SupportViewModel

@ComponentScoped
internal abstract class SupportComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: SupportViewModel
}

internal expect fun FeatureDependencies.Companion.createSupportComponent(
    applicationDependencies: ApplicationComponent
): SupportComponent
