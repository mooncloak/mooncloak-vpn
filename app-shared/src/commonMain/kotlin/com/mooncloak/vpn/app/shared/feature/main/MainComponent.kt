package com.mooncloak.vpn.app.shared.feature.main

import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class MainComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
    @get:Provides internal val navController: NavController
) : FeatureDependencies {

    abstract override val viewModel: MainViewModel
}

internal expect fun FeatureDependencies.Companion.createMainComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    navController: NavController
): MainComponent
