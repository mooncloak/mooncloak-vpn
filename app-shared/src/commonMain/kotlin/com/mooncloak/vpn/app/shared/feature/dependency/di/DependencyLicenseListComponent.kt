package com.mooncloak.vpn.app.shared.feature.dependency.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.dependency.DependencyLicenseListViewModel
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader

@ComponentScoped
internal abstract class DependencyLicenseListComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: DependencyLicenseListViewModel

    abstract val libsLoader: LibsLoader
}

internal expect fun FeatureDependencies.Companion.createDependencyLicenseListComponent(
    applicationDependencies: ApplicationComponent
): DependencyLicenseListComponent
