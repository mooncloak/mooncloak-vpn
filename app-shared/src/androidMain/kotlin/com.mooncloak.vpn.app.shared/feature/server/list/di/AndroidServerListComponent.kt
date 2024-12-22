package com.mooncloak.vpn.app.shared.feature.server.list.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class AndroidServerListComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
) : ServerListComponent()

internal actual fun FeatureDependencies.Companion.createServerListComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): ServerListComponent = AndroidServerListComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
