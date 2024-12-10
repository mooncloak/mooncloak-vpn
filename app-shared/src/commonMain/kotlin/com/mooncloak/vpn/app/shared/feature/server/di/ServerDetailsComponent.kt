package com.mooncloak.vpn.app.shared.feature.server.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.server.ServerDetailsViewModel

@ComponentScoped
internal abstract class ServerDetailsComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: ServerDetailsViewModel
}

internal expect fun FeatureDependencies.Companion.createServerDetailsComponent(
    applicationDependencies: ApplicationComponent
): ServerDetailsComponent
