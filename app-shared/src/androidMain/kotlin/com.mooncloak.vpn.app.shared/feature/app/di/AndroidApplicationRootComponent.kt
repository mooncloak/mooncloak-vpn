package com.mooncloak.vpn.app.shared.feature.app.di

import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies

@Component
@ComponentScoped
internal abstract class AndroidApplicationRootComponent internal constructor(
    @Component internal val applicationDependencies: ApplicationComponent,
    @get:Provides override val navController: NavController
) : ApplicationRootComponent()

internal actual fun FeatureDependencies.Companion.createApplicationRootComponent(
    applicationDependencies: ApplicationComponent,
    navController: NavController
): ApplicationRootComponent = AndroidApplicationRootComponent::class.create(
    applicationDependencies = applicationDependencies,
    navController = navController
)
