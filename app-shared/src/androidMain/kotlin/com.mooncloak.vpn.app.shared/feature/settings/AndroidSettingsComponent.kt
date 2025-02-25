package com.mooncloak.vpn.app.shared.feature.settings

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createSettingsComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): SettingsComponent = SettingsComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
