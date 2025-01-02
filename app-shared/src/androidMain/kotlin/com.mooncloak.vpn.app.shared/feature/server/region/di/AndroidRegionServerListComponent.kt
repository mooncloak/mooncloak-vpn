package com.mooncloak.vpn.app.shared.feature.server.region.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class AndroidRegionServerListComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
) : RegionServerListComponent()

internal actual fun FeatureDependencies.Companion.createRegionServerListComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): RegionServerListComponent = AndroidRegionServerListComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
