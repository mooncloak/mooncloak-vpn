package com.mooncloak.vpn.app.shared.feature.country.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@ComponentScoped
internal abstract class AndroidCountryListComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent
) : CountryListComponent()

internal actual fun FeatureDependencies.Companion.createCountryListComponent(
    applicationDependencies: ApplicationComponent
): CountryListComponent = AndroidCountryListComponent::class.create(
    applicationDependencies = applicationDependencies
)
