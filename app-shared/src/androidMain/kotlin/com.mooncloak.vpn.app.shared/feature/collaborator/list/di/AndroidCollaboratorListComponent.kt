package com.mooncloak.vpn.app.shared.feature.collaborator.list.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class AndroidCollaboratorListComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
) : CollaboratorListComponent()

internal actual fun FeatureDependencies.Companion.createCollaboratorListComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): CollaboratorListComponent = AndroidCollaboratorListComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
