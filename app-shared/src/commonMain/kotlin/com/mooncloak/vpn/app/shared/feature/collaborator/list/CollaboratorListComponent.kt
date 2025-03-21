package com.mooncloak.vpn.app.shared.feature.collaborator.list

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.collaborator.shared.repository.ContributorRepository
import com.mooncloak.vpn.app.shared.feature.collaborator.shared.source.ContributorSource

@Component
@FeatureScoped
internal abstract class CollaboratorListComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
) : FeatureDependencies {

    abstract override val viewModel: CollaboratorListViewModel

    @Provides
    @FeatureScoped
    public fun provideContributorRepository(source: ContributorSource): ContributorRepository = source
}

internal expect fun FeatureDependencies.Companion.createCollaboratorListComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): CollaboratorListComponent
