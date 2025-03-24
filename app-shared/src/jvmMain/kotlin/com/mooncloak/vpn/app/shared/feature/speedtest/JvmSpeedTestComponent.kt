package com.mooncloak.vpn.app.shared.feature.speedtest

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.settings.create

internal actual fun FeatureDependencies.Companion.createSpeedTestComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): SpeedTestComponent = SpeedTestComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
