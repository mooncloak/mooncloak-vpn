package com.mooncloak.vpn.app.shared.feature.home

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createHomeComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): HomeComponent = HomeComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
