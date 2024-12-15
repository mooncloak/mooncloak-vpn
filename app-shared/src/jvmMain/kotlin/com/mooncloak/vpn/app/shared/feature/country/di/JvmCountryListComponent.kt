package com.mooncloak.vpn.app.shared.feature.country.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class JvmCountryListComponent internal constructor(
    @Component internal val presentationDependencies: PresentationComponent
) : CountryListComponent()

internal actual fun FeatureDependencies.Companion.createCountryListComponent(
    presentationDependencies: PresentationComponent
): CountryListComponent = JvmCountryListComponent::class.create(
    presentationDependencies = presentationDependencies
)
