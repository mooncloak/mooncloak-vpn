package com.mooncloak.vpn.app.shared.feature.server.connection

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class ServerConnectionComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
) : FeatureDependencies {

    abstract override val viewModel: ServerConnectionViewModel
}

internal expect fun FeatureDependencies.Companion.createServerConnectionComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): ServerConnectionComponent
