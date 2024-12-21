package com.mooncloak.vpn.app.shared.feature.collaborator.container.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.collaborator.container.CollaboratorContainerViewModel

@ComponentScoped
internal abstract class CollaboratorContainerComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: CollaboratorContainerViewModel
}

internal expect fun FeatureDependencies.Companion.createCollaboratorContainerComponent(
    applicationDependencies: ApplicationComponent
): CollaboratorContainerComponent
