package com.mooncloak.vpn.app.shared.feature.server.connection

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createServerConnectionComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): ServerConnectionComponent = ServerConnectionComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
