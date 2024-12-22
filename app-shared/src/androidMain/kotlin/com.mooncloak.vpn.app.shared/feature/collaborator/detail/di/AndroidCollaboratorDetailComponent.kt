package com.mooncloak.vpn.app.shared.feature.collaborator.detail.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@FeatureScoped
internal abstract class AndroidCollaboratorDetailComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : CollaboratorDetailComponent()

internal actual fun FeatureDependencies.Companion.createCollaboratorDetailComponent(
    applicationDependencies: ApplicationComponent
): CollaboratorDetailComponent = AndroidCollaboratorDetailComponent::class.create(
    applicationDependencies = applicationDependencies
)
