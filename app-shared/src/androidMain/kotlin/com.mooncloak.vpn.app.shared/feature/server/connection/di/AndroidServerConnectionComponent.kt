package com.mooncloak.vpn.app.shared.feature.server.connection.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class AndroidServerConnectionComponent internal constructor(
    @Component internal val presentationDependencies: PresentationComponent
) : ServerConnectionComponent()

internal actual fun FeatureDependencies.Companion.createServerConnectionComponent(
    presentationDependencies: PresentationComponent
): ServerConnectionComponent = AndroidServerConnectionComponent::class.create(
    presentationDependencies = presentationDependencies
)
