package com.mooncloak.vpn.app.shared.feature.settings.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@ComponentScoped
internal abstract class AndroidSettingsComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : SettingsComponent()

internal actual fun FeatureDependencies.Companion.createSettingsComponent(
    applicationDependencies: ApplicationComponent
): SettingsComponent = AndroidSettingsComponent::class.create(
    applicationDependencies = applicationDependencies
)
