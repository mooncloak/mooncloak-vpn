package com.mooncloak.vpn.app.shared.feature.app

import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class ApplicationRootComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
    @get:Provides val navController: NavController
) : FeatureDependencies {

    abstract override val viewModel: ApplicationRootViewModel
}

internal expect fun FeatureDependencies.Companion.createApplicationRootComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    navController: NavController
): ApplicationRootComponent
