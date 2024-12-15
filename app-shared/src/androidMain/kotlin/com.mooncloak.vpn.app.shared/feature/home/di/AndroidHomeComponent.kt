package com.mooncloak.vpn.app.shared.feature.home.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class AndroidHomeComponent internal constructor(
    @Component internal val presentationDependencies: PresentationComponent
) : HomeComponent()

internal actual fun FeatureDependencies.Companion.createHomeComponent(
    presentationDependencies: PresentationComponent
): HomeComponent = AndroidHomeComponent::class.create(
    presentationDependencies = presentationDependencies
)
