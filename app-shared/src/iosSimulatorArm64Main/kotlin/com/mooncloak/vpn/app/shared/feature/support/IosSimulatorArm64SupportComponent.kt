package com.mooncloak.vpn.app.shared.feature.support

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createSupportComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): SupportComponent = SupportComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
