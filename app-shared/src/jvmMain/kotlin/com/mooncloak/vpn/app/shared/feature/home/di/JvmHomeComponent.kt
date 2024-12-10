package com.mooncloak.vpn.app.shared.feature.home.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@ComponentScoped
internal abstract class JvmHomeComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : HomeComponent()

internal actual fun FeatureDependencies.Companion.createHomeComponent(
    applicationDependencies: ApplicationComponent
): HomeComponent = JvmHomeComponent::class.create(
    applicationDependencies = applicationDependencies
)
