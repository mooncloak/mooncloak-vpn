package com.mooncloak.vpn.app.shared.feature.main.di

import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class JvmMainComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
    @get:Provides override val navController: NavController
) : MainComponent()

internal actual fun FeatureDependencies.Companion.createMainComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    navController: NavController
): MainComponent = JvmMainComponent::class.create(
    applicationComponent = applicationComponent,
    presentationComponent = presentationComponent,
    navController = navController
)
