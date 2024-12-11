package com.mooncloak.vpn.app.shared.feature.dependency.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.dependency.DependencyLicenseListViewModel

@ComponentScoped
internal abstract class DependencyLicenseListComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: DependencyLicenseListViewModel
}

internal expect fun FeatureDependencies.Companion.createDependencyLicenseListComponent(
    applicationDependencies: ApplicationComponent
): DependencyLicenseListComponent
