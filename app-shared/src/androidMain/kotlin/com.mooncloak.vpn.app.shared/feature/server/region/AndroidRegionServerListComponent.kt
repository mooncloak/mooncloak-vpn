package com.mooncloak.vpn.app.shared.feature.server.region

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createRegionServerListComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): RegionServerListComponent = RegionServerListComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
