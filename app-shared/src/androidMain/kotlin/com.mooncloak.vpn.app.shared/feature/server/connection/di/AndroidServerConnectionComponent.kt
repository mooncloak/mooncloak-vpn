package com.mooncloak.vpn.app.shared.feature.server.connection.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.server.di.create

@Component
@ComponentScoped
internal abstract class AndroidServerConnectionComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : ServerConnectionComponent()

internal actual fun FeatureDependencies.Companion.createServerConnectionComponent(
    applicationDependencies: ApplicationComponent
): ServerConnectionComponent = AndroidServerConnectionComponent::class.create(
    applicationDependencies = applicationDependencies
)
