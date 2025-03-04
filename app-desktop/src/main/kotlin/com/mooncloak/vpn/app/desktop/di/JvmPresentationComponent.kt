package com.mooncloak.vpn.app.desktop.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.desktop.api.server.JvmVPNConnectionManager
import com.mooncloak.vpn.api.shared.billing.BillingManager
import com.mooncloak.vpn.app.shared.api.billing.MooncloakBillingManager
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansApiSource
import com.mooncloak.vpn.api.shared.plan.ServicePlansRepository
import com.mooncloak.vpn.api.shared.tunnel.TunnelManager
import com.mooncloak.vpn.api.shared.tunnel.TunnelManagerPreparer
import com.mooncloak.vpn.api.shared.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.di.PresentationScoped
import com.mooncloak.vpn.app.shared.util.JvmLibsLoader
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider
import com.mooncloak.vpn.util.shared.coroutine.PresentationCoroutineScope
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

    @Provides
    @PresentationScoped
    internal fun provideLibsLoader(loader: JvmLibsLoader): LibsLoader = loader

    @Provides
    @PresentationScoped
    internal fun provideTunnelManagerPreparer(tunnelManager: TunnelManager): TunnelManagerPreparer =
        TunnelManagerPreparer { true }
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
