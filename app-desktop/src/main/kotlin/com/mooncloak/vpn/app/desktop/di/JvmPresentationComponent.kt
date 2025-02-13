package com.mooncloak.vpn.app.desktop.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.desktop.api.server.JvmVPNConnectionManager
import com.mooncloak.vpn.app.shared.api.billing.BillingManager
import com.mooncloak.vpn.app.shared.api.billing.MooncloakBillingManager
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansApiSource
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansRepository
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.di.PresentationScoped
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider
import com.mooncloak.vpn.app.shared.util.coroutine.PresentationCoroutineScope
import com.mooncloak.vpn.app.shared.util.invoke

@Component
@PresentationScoped
internal abstract class JvmPresentationComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @get:Provides override val presentationCoroutineScope: PresentationCoroutineScope,
    @get:Provides override val uriHandler: UriHandler
) : PresentationComponent() {

    @Provides
    @PresentationScoped
    internal fun provideBillingManager(manager: MooncloakBillingManager): BillingManager = manager

    @Provides
    @PresentationScoped
    internal fun providePlansRepository(source: ServicePlansApiSource): ServicePlansRepository = source

    @Provides
    @PresentationScoped
    internal fun provideSystemAuthenticationProvider(): SystemAuthenticationProvider =
        SystemAuthenticationProvider()

    @Provides
    @PresentationScoped
    internal fun provideServerConnectionManager(manager: JvmVPNConnectionManager): VPNConnectionManager =
        manager
}

internal fun PresentationComponent.Companion.create(
    applicationComponent: ApplicationComponent,
    presentationCoroutineScope: PresentationCoroutineScope,
    uriHandler: UriHandler
): JvmPresentationComponent = JvmPresentationComponent::class.create(
    applicationComponent = applicationComponent,
    presentationCoroutineScope = presentationCoroutineScope,
    uriHandler = uriHandler
)
