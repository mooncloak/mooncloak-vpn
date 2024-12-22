package com.mooncloak.vpn.app.shared.feature.server.connection.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@FeatureScoped
internal abstract class AndroidServerConnectionComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : ServerConnectionComponent()

internal actual fun FeatureDependencies.Companion.createServerConnectionComponent(
    applicationDependencies: ApplicationComponent
): ServerConnectionComponent = AndroidServerConnectionComponent::class.create(
    applicationDependencies = applicationDependencies
)
