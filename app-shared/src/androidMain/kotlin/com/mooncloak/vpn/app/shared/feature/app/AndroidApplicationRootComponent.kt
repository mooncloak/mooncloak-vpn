package com.mooncloak.vpn.app.shared.feature.app

import androidx.navigation.NavController
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createApplicationRootComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    navController: NavController
): ApplicationRootComponent =
    ApplicationRootComponent::class.create(
        applicationComponent = applicationComponent,
        presentationComponent = presentationComponent,
        navController = navController
    )
