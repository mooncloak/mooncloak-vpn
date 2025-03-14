package com.mooncloak.vpn.app.shared.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.api.shared.billing.BillingManager
import com.mooncloak.vpn.app.shared.api.billing.MooncloakBillingManager
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansApiSource
import com.mooncloak.vpn.api.shared.plan.ServicePlansRepository
import com.mooncloak.vpn.app.shared.api.server.invoke
import com.mooncloak.vpn.network.core.tunnel.TunnelManager
import com.mooncloak.vpn.network.core.tunnel.TunnelManagerPreparer
import com.mooncloak.vpn.network.core.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader
import com.mooncloak.vpn.app.shared.util.IosLibsLoader
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider
import com.mooncloak.vpn.util.shared.coroutine.PresentationCoroutineScope
import com.mooncloak.vpn.app.shared.util.invoke
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import com.mooncloak.vpn.util.shortcuts.invoke
import com.mooncloak.vpn.util.shortcuts.AppShortcutProvider

@Component
@PresentationScoped
internal abstract class IosPresentationComponent internal constructor(
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
    internal fun provideServerConnectionManager(coroutineScope: ApplicationCoroutineScope): VPNConnectionManager =
        VPNConnectionManager.invoke(
            coroutineScope = coroutineScope
        )

    @Provides
    @PresentationScoped
    internal fun provideLibsLoader(loader: IosLibsLoader): LibsLoader = loader

    @Provides
    @PresentationScoped
    internal fun provideTunnelManagerPreparer(tunnelManager: TunnelManager): TunnelManagerPreparer =
        TunnelManagerPreparer { true }

    @Provides
    @PresentationScoped
    internal fun provideAppShortcutProvider(): AppShortcutProvider = AppShortcutProvider()
}

internal expect fun PresentationComponent.Companion.create(
    applicationComponent: ApplicationComponent,
    presentationCoroutineScope: PresentationCoroutineScope,
    uriHandler: UriHandler
): IosPresentationComponent
