package com.mooncloak.vpn.app.shared.feature.main.di

import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@FeatureScoped
internal abstract class AndroidMainComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent,
    @get:Provides override val navController: NavController
) : MainComponent()

internal actual fun FeatureDependencies.Companion.createMainComponent(
    applicationDependencies: ApplicationComponent,
    navController: NavController
): MainComponent = AndroidMainComponent::class.create(
    applicationDependencies = applicationDependencies,
    navController = navController
)
