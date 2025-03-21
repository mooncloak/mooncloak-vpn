package com.mooncloak.vpn.app.shared.feature.collaborator.tip

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.collaborator.tip.create

internal actual fun FeatureDependencies.Companion.createTipComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): TipComponent = TipComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
