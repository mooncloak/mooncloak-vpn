package com.mooncloak.vpn.app.shared.feature.support.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class JvmSupportComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
) : SupportComponent()

internal actual fun FeatureDependencies.Companion.createSupportComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): SupportComponent = JvmSupportComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
