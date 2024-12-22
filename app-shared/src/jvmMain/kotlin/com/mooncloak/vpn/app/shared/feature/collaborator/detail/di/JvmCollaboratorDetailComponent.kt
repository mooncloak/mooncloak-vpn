package com.mooncloak.vpn.app.shared.feature.collaborator.detail.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.collaborator.list.di.CollaboratorListComponent

@Component
@FeatureScoped
internal abstract class JvmCollaboratorDetailComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
) : CollaboratorListComponent()

internal actual fun FeatureDependencies.Companion.createCollaboratorDetailComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): CollaboratorDetailComponent = JvmCollaboratorDetailComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent
)
