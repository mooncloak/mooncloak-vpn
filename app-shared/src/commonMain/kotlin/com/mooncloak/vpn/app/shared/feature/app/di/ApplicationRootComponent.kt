package com.mooncloak.vpn.app.shared.feature.app.di

import androidx.navigation.NavController
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.app.ApplicationRootViewModel

@FeatureScoped
internal abstract class ApplicationRootComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: ApplicationRootViewModel

    abstract val navController: NavController
}

internal expect fun FeatureDependencies.Companion.createApplicationRootComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    navController: NavController
): ApplicationRootComponent
