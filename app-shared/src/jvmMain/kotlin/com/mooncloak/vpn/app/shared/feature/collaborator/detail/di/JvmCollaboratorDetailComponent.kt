package com.mooncloak.vpn.app.shared.feature.collaborator.detail.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.collaborator.list.di.CollaboratorListComponent

@Component
@FeatureScoped
internal abstract class JvmCollaboratorDetailComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : CollaboratorListComponent()

internal actual fun FeatureDependencies.Companion.createCollaboratorDetailComponent(
    applicationDependencies: ApplicationComponent
): CollaboratorDetailComponent = JvmCollaboratorDetailComponent::class.create(
    applicationDependencies = applicationDependencies
)
