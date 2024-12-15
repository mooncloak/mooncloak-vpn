package com.mooncloak.vpn.app.shared.feature.app.di

import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class JvmApplicationRootComponent internal constructor(
    @Component internal val presentationDependencies: PresentationComponent,
    @get:Provides override val navController: NavController
) : ApplicationRootComponent()

internal actual fun FeatureDependencies.Companion.createApplicationRootComponent(
    presentationDependencies: PresentationComponent,
    navController: NavController
): ApplicationRootComponent = JvmApplicationRootComponent::class.create(
    presentationDependencies = presentationDependencies,
    navController = navController
)
