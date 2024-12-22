package com.mooncloak.vpn.app.shared.feature.settings.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class JvmSettingsComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
) : SettingsComponent()

internal actual fun FeatureDependencies.Companion.createSettingsComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): SettingsComponent = JvmSettingsComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
