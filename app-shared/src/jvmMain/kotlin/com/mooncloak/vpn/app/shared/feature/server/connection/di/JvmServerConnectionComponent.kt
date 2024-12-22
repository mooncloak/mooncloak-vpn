package com.mooncloak.vpn.app.shared.feature.server.connection.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.server.di.create

@Component
@FeatureScoped
internal abstract class JvmServerConnectionComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : ServerConnectionComponent()

internal actual fun FeatureDependencies.Companion.createServerConnectionComponent(
    applicationDependencies: ApplicationComponent
): ServerConnectionComponent = JvmServerConnectionComponent::class.create(
    applicationDependencies = applicationDependencies
)
