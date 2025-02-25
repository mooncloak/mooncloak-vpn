package com.mooncloak.vpn.app.shared.feature.dependency

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createDependencyLicenseListComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): DependencyLicenseListComponent = DependencyLicenseListComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
