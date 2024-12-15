package com.mooncloak.vpn.app.shared.feature.server.details.di

import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.server.details.ServerDetailsViewModel

@FeatureScoped
internal abstract class ServerDetailsComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: ServerDetailsViewModel
}

internal expect fun FeatureDependencies.Companion.createServerDetailsComponent(
    presentationDependencies: PresentationComponent
): ServerDetailsComponent
