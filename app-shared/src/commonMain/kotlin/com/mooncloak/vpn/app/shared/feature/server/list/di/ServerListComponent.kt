package com.mooncloak.vpn.app.shared.feature.server.list.di

import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.server.list.ServerListViewModel

@FeatureScoped
internal abstract class ServerListComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: ServerListViewModel
}

internal expect fun FeatureDependencies.Companion.createServerListComponent(
    presentationDependencies: PresentationComponent
): ServerListComponent
