package com.mooncloak.vpn.app.android.di

import android.app.Activity
import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.android.GooglePlayBillingManager
import com.mooncloak.vpn.app.shared.api.billing.BillingManager
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.di.PresentationScoped
import com.mooncloak.vpn.app.shared.util.ActivityContext
import com.mooncloak.vpn.app.shared.util.AndroidSystemAuthenticationProvider
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider

@Component
@PresentationScoped
internal abstract class AndroidPresentationComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @get:Provides override val uriHandler: UriHandler,
    @get:Provides override val activityContext: ActivityContext,
    @get:Provides override val activity: Activity
) : PresentationComponent() {

    @Provides
    @PresentationScoped
    internal fun provideBillingManager(manager: GooglePlayBillingManager): BillingManager = manager

    @Provides
    @PresentationScoped
    internal fun provideSystemAuthenticationProvider(provider: AndroidSystemAuthenticationProvider): SystemAuthenticationProvider =
        provider
}

internal fun PresentationComponent.Companion.create(
    applicationComponent: ApplicationComponent,
    activity: Activity,
    uriHandler: UriHandler
): AndroidPresentationComponent = AndroidPresentationComponent::class.create(
    applicationComponent = applicationComponent,
    activityContext = activity,
    activity = activity,
    uriHandler = uriHandler
)
