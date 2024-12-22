package com.mooncloak.vpn.app.shared.feature.server.details.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.server.di.create

@Component
@FeatureScoped
internal abstract class JvmServerDetailsComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : ServerDetailsComponent()

internal actual fun FeatureDependencies.Companion.createServerDetailsComponent(
    applicationDependencies: ApplicationComponent
): ServerDetailsComponent = JvmServerDetailsComponent::class.create(
    applicationDependencies = applicationDependencies
)
