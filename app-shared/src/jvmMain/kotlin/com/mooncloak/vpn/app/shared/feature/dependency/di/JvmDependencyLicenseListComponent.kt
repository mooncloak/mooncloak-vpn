package com.mooncloak.vpn.app.shared.feature.dependency.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.dependency.util.JvmLibsLoader
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader

@Component
@FeatureScoped
internal abstract class JvmDependencyLicenseListComponent internal constructor(
    @Component internal val presentationDependencies: PresentationComponent
) : DependencyLicenseListComponent() {

    @Provides
    @FeatureScoped
    internal fun provideLibsLoader(libsLoader: JvmLibsLoader): LibsLoader = libsLoader
}

internal actual fun FeatureDependencies.Companion.createDependencyLicenseListComponent(
    presentationDependencies: PresentationComponent
): DependencyLicenseListComponent = JvmDependencyLicenseListComponent::class.create(
    presentationDependencies = presentationDependencies
)
