package com.mooncloak.vpn.app.shared.feature.dependency.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@ComponentScoped
internal abstract class AndroidDependencyLicenseListComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : DependencyLicenseListComponent()

internal actual fun FeatureDependencies.Companion.createDependencyLicenseListComponent(
    applicationDependencies: ApplicationComponent
): DependencyLicenseListComponent = AndroidDependencyLicenseListComponent::class.create(
    applicationDependencies = applicationDependencies
)
