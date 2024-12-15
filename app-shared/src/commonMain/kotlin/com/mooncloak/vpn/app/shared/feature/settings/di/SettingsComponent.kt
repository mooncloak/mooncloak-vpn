package com.mooncloak.vpn.app.shared.feature.settings.di

import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.settings.SettingsViewModel

@FeatureScoped
internal abstract class SettingsComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: SettingsViewModel
}

internal expect fun FeatureDependencies.Companion.createSettingsComponent(
    presentationDependencies: PresentationComponent
): SettingsComponent
