package com.mooncloak.vpn.app.shared.feature.server.details.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class AndroidServerDetailsComponent internal constructor(
    @Component internal val presentationDependencies: PresentationComponent
) : ServerDetailsComponent()

internal actual fun FeatureDependencies.Companion.createServerDetailsComponent(
    presentationDependencies: PresentationComponent
): ServerDetailsComponent = AndroidServerDetailsComponent::class.create(
    presentationDependencies = presentationDependencies
)
