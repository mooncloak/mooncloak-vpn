package com.mooncloak.vpn.app.shared.feature.server.list.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@ComponentScoped
internal abstract class AndroidServerListComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : ServerListComponent()

internal actual fun FeatureDependencies.Companion.createServerListComponent(
    applicationDependencies: ApplicationComponent
): ServerListComponent = AndroidServerListComponent::class.create(
    applicationDependencies = applicationDependencies
)
