package com.mooncloak.vpn.app.android.di

import android.app.Activity
import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.android.play.GooglePlayBillingManager
import com.mooncloak.vpn.app.android.api.server.AndroidVPNConnectionManager
import com.mooncloak.vpn.app.android.play.ServicePlansGooglePlaySource
import com.mooncloak.vpn.api.shared.billing.BillingManager
import com.mooncloak.vpn.api.shared.plan.ServicePlansRepository
import com.mooncloak.vpn.api.shared.tunnel.TunnelManager
import com.mooncloak.vpn.api.shared.tunnel.TunnelManagerPreparer
import com.mooncloak.vpn.api.shared.tunnel.invoke
import com.mooncloak.vpn.api.shared.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.di.PresentationScoped
import com.mooncloak.vpn.app.shared.util.AndroidLibsLoader
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader
import com.mooncloak.vpn.app.shared.util.ActivityContext
import com.mooncloak.vpn.app.shared.util.AndroidSystemAuthenticationProvider
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider
import com.mooncloak.vpn.util.shared.coroutine.PresentationCoroutineScope

@Component
@PresentationScoped
internal abstract class AndroidPresentationComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @get:Provides override val presentationCoroutineScope: PresentationCoroutineScope,
    @get:Provides override val uriHandler: UriHandler,
    @get:Provides override val activityContext: ActivityContext,
    @get:Provides override val activity: Activity
) : PresentationComponent() {

    @Provides
    @PresentationScoped
    internal fun provideBillingManager(manager: GooglePlayBillingManager): BillingManager = manager

    @Provides
    @PresentationScoped
    internal fun providePlansRepository(source: ServicePlansGooglePlaySource): ServicePlansRepository = source

    @Provides
    @PresentationScoped
    internal fun provideSystemAuthenticationProvider(provider: AndroidSystemAuthenticationProvider): SystemAuthenticationProvider =
        provider

    @Provides
    @PresentationScoped
    internal fun provideServerConnectionManager(manager: AndroidVPNConnectionManager): VPNConnectionManager =
        manager

    @Provides
    @PresentationScoped
    internal fun provideLibsLoader(loader: AndroidLibsLoader): LibsLoader = loader

    @Provides
    @PresentationScoped
    internal fun provideTunnelManagerPreparer(tunnelManager: TunnelManager): TunnelManagerPreparer =
        TunnelManagerPreparer(
            tunnelManager = tunnelManager,
            activity = activity
        )
}

internal fun PresentationComponent.Companion.create(
    applicationComponent: ApplicationComponent,
    coroutineScope: PresentationCoroutineScope,
    activity: Activity,
    uriHandler: UriHandler
): AndroidPresentationComponent = AndroidPresentationComponent::class.create(
    applicationComponent = applicationComponent,
    presentationCoroutineScope = coroutineScope,
    activityContext = activity,
    activity = activity,
    uriHandler = uriHandler
)
