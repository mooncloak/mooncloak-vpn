package com.mooncloak.vpn.app.shared.feature.server.details.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@ComponentScoped
internal abstract class AndroidServerDetailsComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : ServerDetailsComponent()

internal actual fun FeatureDependencies.Companion.createServerDetailsComponent(
    applicationDependencies: ApplicationComponent
): ServerDetailsComponent = AndroidServerDetailsComponent::class.create(
    applicationDependencies = applicationDependencies
)
