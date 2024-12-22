package com.mooncloak.vpn.app.shared.feature.dependency.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.dependency.util.AndroidLibsLoader
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader

@Component
@FeatureScoped
internal abstract class AndroidDependencyLicenseListComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : DependencyLicenseListComponent() {

    @Provides
    @FeatureScoped
    internal fun provideLibsLoader(libsLoader: AndroidLibsLoader): LibsLoader = libsLoader
}

internal actual fun FeatureDependencies.Companion.createDependencyLicenseListComponent(
    applicationDependencies: ApplicationComponent
): DependencyLicenseListComponent = AndroidDependencyLicenseListComponent::class.create(
    applicationDependencies = applicationDependencies
)
