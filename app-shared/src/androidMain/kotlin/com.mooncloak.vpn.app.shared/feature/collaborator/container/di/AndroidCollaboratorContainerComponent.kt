package com.mooncloak.vpn.app.shared.feature.collaborator.container.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class AndroidCollaboratorContainerComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
) : CollaboratorContainerComponent()

internal actual fun FeatureDependencies.Companion.createCollaboratorContainerComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent
): CollaboratorContainerComponent = AndroidCollaboratorContainerComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
