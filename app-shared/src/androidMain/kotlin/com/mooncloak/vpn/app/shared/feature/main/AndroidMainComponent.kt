package com.mooncloak.vpn.app.shared.feature.main

import androidx.navigation.NavController
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

internal actual fun FeatureDependencies.Companion.createMainComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    navController: NavController
): MainComponent = MainComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent,
    navController = navController
)
