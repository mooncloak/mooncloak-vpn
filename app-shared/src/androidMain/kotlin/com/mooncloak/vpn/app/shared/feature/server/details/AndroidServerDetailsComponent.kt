package com.mooncloak.vpn.app.shared.feature.server.details

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createServerDetailsComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): ServerDetailsComponent = ServerDetailsComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
