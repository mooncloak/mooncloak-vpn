package com.mooncloak.vpn.app.shared.feature.support.di

import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.support.SupportViewModel

@FeatureScoped
internal abstract class SupportComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: SupportViewModel
}

internal expect fun FeatureDependencies.Companion.createSupportComponent(
    presentationDependencies: PresentationComponent
): SupportComponent
