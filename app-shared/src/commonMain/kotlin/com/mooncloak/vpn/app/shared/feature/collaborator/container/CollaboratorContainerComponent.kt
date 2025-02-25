package com.mooncloak.vpn.app.shared.feature.collaborator.container

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class CollaboratorContainerComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
) : FeatureDependencies {

    abstract override val viewModel: CollaboratorContainerViewModel
}

internal expect fun FeatureDependencies.Companion.createCollaboratorContainerComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent
): CollaboratorContainerComponent
