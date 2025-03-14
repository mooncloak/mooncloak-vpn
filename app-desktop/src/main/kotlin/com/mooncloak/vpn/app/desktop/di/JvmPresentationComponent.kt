package com.mooncloak.vpn.app.desktop.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.api.shared.billing.BillingManager
import com.mooncloak.vpn.app.shared.api.billing.MooncloakBillingManager
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansApiSource
import com.mooncloak.vpn.api.shared.plan.ServicePlansRepository
import com.mooncloak.vpn.api.shared.server.ServerConnectionRecordRepository
import com.mooncloak.vpn.network.core.tunnel.TunnelManager
import com.mooncloak.vpn.network.core.tunnel.TunnelManagerPreparer
import com.mooncloak.vpn.network.core.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.di.PresentationScoped
import com.mooncloak.vpn.app.shared.util.JvmLibsLoader
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider
import com.mooncloak.vpn.util.shared.coroutine.PresentationCoroutineScope
import com.mooncloak.vpn.app.shared.util.invoke
import com.mooncloak.vpn.network.core.vpn.BaseVPNConnectionManager
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import com.mooncloak.vpn.util.shortcuts.invoke
import com.mooncloak.vpn.util.shortcuts.AppShortcutProvider
import kotlinx.datetime.Clock

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
    internal fun provideServerConnectionManager(
        coroutineScope: ApplicationCoroutineScope,
        serverConnectionRecordRepository: ServerConnectionRecordRepository,
        clock: Clock,
        tunnelManager: TunnelManager
    ): VPNConnectionManager =
        BaseVPNConnectionManager(
            coroutineScope = coroutineScope,
            serverConnectionRecordRepository = serverConnectionRecordRepository,
            clock = clock,
            tunnelManager = tunnelManager
        )

    @Provides
    @PresentationScoped
    internal fun provideLibsLoader(loader: JvmLibsLoader): LibsLoader = loader

    @Provides
    @PresentationScoped
    internal fun provideTunnelManagerPreparer(tunnelManager: TunnelManager): TunnelManagerPreparer =
        TunnelManagerPreparer { true }

    @Provides
    @PresentationScoped
    internal fun provideAppShortcutProvider(): AppShortcutProvider = AppShortcutProvider()
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
