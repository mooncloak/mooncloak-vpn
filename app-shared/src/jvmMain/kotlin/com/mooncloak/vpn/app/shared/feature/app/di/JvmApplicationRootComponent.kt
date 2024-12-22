package com.mooncloak.vpn.app.shared.feature.app.di

import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class JvmApplicationRootComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
    @get:Provides override val navController: NavController
) : ApplicationRootComponent()

internal actual fun FeatureDependencies.Companion.createApplicationRootComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    navController: NavController
): ApplicationRootComponent = JvmApplicationRootComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent,
    navController = navController
)
