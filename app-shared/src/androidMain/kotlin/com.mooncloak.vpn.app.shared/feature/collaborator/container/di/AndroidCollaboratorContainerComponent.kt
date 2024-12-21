package com.mooncloak.vpn.app.shared.feature.collaborator.container.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@ComponentScoped
internal abstract class AndroidCollaboratorContainerComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : CollaboratorContainerComponent()

internal actual fun FeatureDependencies.Companion.createCollaboratorContainerComponent(
    applicationDependencies: ApplicationComponent
): CollaboratorContainerComponent = AndroidCollaboratorContainerComponent::class.create(
    applicationDependencies = applicationDependencies
)
