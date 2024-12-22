package com.mooncloak.vpn.app.shared.feature.server.connection.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.server.connection.ServerConnectionViewModel

@FeatureScoped
internal abstract class ServerConnectionComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: ServerConnectionViewModel
}

internal expect fun FeatureDependencies.Companion.createServerConnectionComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): ServerConnectionComponent
