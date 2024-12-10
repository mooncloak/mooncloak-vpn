package com.mooncloak.vpn.app.shared.feature.settings.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.settings.SettingsViewModel

@ComponentScoped
internal abstract class SettingsComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: SettingsViewModel
}

internal expect fun FeatureDependencies.Companion.createSettingsComponent(
    applicationDependencies: ApplicationComponent
): SettingsComponent
