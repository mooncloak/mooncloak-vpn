package com.mooncloak.vpn.app.shared.feature.country.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.country.CountryListViewModel

@FeatureScoped
internal abstract class CountryListComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: CountryListViewModel
}

internal expect fun FeatureDependencies.Companion.createCountryListComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): CountryListComponent
