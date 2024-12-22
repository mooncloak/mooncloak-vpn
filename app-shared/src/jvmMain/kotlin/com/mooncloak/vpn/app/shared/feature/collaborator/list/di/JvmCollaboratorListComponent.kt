package com.mooncloak.vpn.app.shared.feature.collaborator.list.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@FeatureScoped
internal abstract class JvmCollaboratorListComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : CollaboratorListComponent()

internal actual fun FeatureDependencies.Companion.createCollaboratorListComponent(
    applicationDependencies: ApplicationComponent
): CollaboratorListComponent = JvmCollaboratorListComponent::class.create(
    applicationDependencies = applicationDependencies
)
