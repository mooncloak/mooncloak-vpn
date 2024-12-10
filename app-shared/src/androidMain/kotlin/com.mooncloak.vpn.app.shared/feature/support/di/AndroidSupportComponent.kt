package com.mooncloak.vpn.app.shared.feature.support.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@ComponentScoped
internal abstract class AndroidSupportComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : SupportComponent()

internal actual fun FeatureDependencies.Companion.createSupportComponent(
    applicationDependencies: ApplicationComponent
): SupportComponent = AndroidSupportComponent::class.create(
    applicationDependencies = applicationDependencies
)
