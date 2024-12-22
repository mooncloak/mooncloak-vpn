package com.mooncloak.vpn.app.desktop.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.di.PresentationScoped

@Component
@PresentationScoped
internal abstract class JvmPresentationComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @get:Provides override val uriHandler: UriHandler
) : PresentationComponent()

internal fun PresentationComponent.Companion.create(
    applicationComponent: ApplicationComponent,
    uriHandler: UriHandler
): JvmPresentationComponent = JvmPresentationComponent::class.create(
    applicationComponent = applicationComponent,
    uriHandler = uriHandler
)
