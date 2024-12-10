package com.mooncloak.vpn.app.shared.feature.main.di

import androidx.navigation.NavController
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.feature.main.MainViewModel

@ComponentScoped
internal abstract class MainComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: MainViewModel

    abstract val navController: NavController
}

internal expect fun FeatureDependencies.Companion.createMainComponent(
    applicationDependencies: ApplicationComponent,
    navController: NavController
): MainComponent
