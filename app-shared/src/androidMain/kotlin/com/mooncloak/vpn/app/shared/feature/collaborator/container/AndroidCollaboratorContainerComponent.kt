package com.mooncloak.vpn.app.shared.feature.collaborator.container

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createCollaboratorContainerComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent
): CollaboratorContainerComponent = CollaboratorContainerComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
