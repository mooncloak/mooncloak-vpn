package com.mooncloak.vpn.app.shared.feature.collaborator.detail.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.collaborator.detail.CollaboratorDetailViewModel

@FeatureScoped
internal abstract class CollaboratorDetailComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: CollaboratorDetailViewModel
}

internal expect fun FeatureDependencies.Companion.createCollaboratorDetailComponent(
    applicationDependencies: ApplicationComponent
): CollaboratorDetailComponent
