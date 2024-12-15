package com.mooncloak.vpn.app.shared.feature.support.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class JvmSupportComponent internal constructor(
    @Component internal val presentationDependencies: PresentationComponent
) : SupportComponent()

internal actual fun FeatureDependencies.Companion.createSupportComponent(
    presentationDependencies: PresentationComponent
): SupportComponent = JvmSupportComponent::class.create(
    presentationDependencies = presentationDependencies
)
