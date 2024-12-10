package com.mooncloak.vpn.app.shared.feature.app.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@ComponentScoped
internal abstract class AndroidApplicationRootComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : ApplicationRootComponent()

internal actual fun FeatureDependencies.Companion.createApplicationRootComponent(
    applicationDependencies: ApplicationComponent
): ApplicationRootComponent = AndroidApplicationRootComponent::class.create(
    applicationDependencies = applicationDependencies
)
