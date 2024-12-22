package com.mooncloak.vpn.app.shared.feature.support.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@FeatureScoped
internal abstract class JvmSupportComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : SupportComponent()

internal actual fun FeatureDependencies.Companion.createSupportComponent(
    applicationDependencies: ApplicationComponent
): SupportComponent = JvmSupportComponent::class.create(
    applicationDependencies = applicationDependencies
)
