package com.mooncloak.vpn.app.shared.feature.home.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class JvmHomeComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
) : HomeComponent()

internal actual fun FeatureDependencies.Companion.createHomeComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): HomeComponent = JvmHomeComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
