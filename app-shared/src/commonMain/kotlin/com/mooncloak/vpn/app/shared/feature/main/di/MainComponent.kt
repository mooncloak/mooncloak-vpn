package com.mooncloak.vpn.app.shared.feature.main.di

import androidx.navigation.NavController
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.main.MainViewModel

@FeatureScoped
internal abstract class MainComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: MainViewModel

    abstract val navController: NavController
}

internal expect fun FeatureDependencies.Companion.createMainComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    navController: NavController
): MainComponent
