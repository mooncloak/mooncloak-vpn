package com.mooncloak.vpn.app.shared.feature.collaborator.detail

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createCollaboratorDetailComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent
): CollaboratorDetailComponent = CollaboratorDetailComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
