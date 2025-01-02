package com.mooncloak.vpn.app.shared.feature.server.region.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.server.region.RegionServerListViewModel

@FeatureScoped
internal abstract class RegionServerListComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: RegionServerListViewModel
}

internal expect fun FeatureDependencies.Companion.createRegionServerListComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): RegionServerListComponent
