package com.mooncloak.vpn.app.shared.feature.collaborator.list.di

import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.collaborator.list.CollaboratorListViewModel
import com.mooncloak.vpn.app.shared.feature.collaborator.repository.ContributorRepository
import com.mooncloak.vpn.app.shared.feature.collaborator.source.ContributorApiSource

@FeatureScoped
internal abstract class CollaboratorListComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: CollaboratorListViewModel

    @Provides
    internal fun provideCollaboratorRepository(source: ContributorApiSource): ContributorRepository = source
}

internal expect fun FeatureDependencies.Companion.createCollaboratorListComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): CollaboratorListComponent
