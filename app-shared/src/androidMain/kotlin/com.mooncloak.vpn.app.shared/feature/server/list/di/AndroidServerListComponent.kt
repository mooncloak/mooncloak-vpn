package com.mooncloak.vpn.app.shared.feature.server.list.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class AndroidServerListComponent internal constructor(
    @Component internal val presentationDependencies: PresentationComponent
) : ServerListComponent()

internal actual fun FeatureDependencies.Companion.createServerListComponent(
    presentationDependencies: PresentationComponent
): ServerListComponent = AndroidServerListComponent::class.create(
    presentationDependencies = presentationDependencies
)
