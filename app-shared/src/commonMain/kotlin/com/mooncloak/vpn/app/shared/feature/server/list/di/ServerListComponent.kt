package com.mooncloak.vpn.app.shared.feature.server.list.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.server.list.ServerListViewModel

@ComponentScoped
internal abstract class ServerListComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: ServerListViewModel
}

internal expect fun FeatureDependencies.Companion.createServerListComponent(
    applicationDependencies: ApplicationComponent
): ServerListComponent
