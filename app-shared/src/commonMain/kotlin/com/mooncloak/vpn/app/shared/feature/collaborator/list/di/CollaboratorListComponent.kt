package com.mooncloak.vpn.app.shared.feature.collaborator.list.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.collaborator.list.CollaboratorListViewModel

@ComponentScoped
internal abstract class CollaboratorListComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: CollaboratorListViewModel
}

internal expect fun FeatureDependencies.Companion.createCollaboratorListComponent(
    applicationDependencies: ApplicationComponent
): CollaboratorListComponent
