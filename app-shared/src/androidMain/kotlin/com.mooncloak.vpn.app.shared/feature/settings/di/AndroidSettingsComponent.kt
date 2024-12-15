package com.mooncloak.vpn.app.shared.feature.settings.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class AndroidSettingsComponent internal constructor(
    @Component internal val presentationDependencies: PresentationComponent
) : SettingsComponent()

internal actual fun FeatureDependencies.Companion.createSettingsComponent(
    presentationDependencies: PresentationComponent
): SettingsComponent = AndroidSettingsComponent::class.create(
    presentationDependencies = presentationDependencies
)
