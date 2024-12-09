package com.mooncloak.vpn.app.shared.feature.country.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.country.CountryListViewModel

@ComponentScoped
internal abstract class CountryListComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: CountryListViewModel
}

internal expect fun FeatureDependencies.Companion.createCountryListComponent(
    applicationDependencies: ApplicationComponent
): CountryListComponent
