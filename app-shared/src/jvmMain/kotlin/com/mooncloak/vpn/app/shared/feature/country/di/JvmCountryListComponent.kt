package com.mooncloak.vpn.app.shared.feature.country.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@FeatureScoped
internal abstract class JvmCountryListComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : CountryListComponent()

internal actual fun FeatureDependencies.Companion.createCountryListComponent(
    applicationDependencies: ApplicationComponent
): CountryListComponent = JvmCountryListComponent::class.create(
    applicationDependencies = applicationDependencies
)
