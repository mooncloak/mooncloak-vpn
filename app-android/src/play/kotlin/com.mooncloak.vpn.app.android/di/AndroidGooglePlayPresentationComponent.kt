package com.mooncloak.vpn.app.android.di

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.api.shared.billing.BillingManager
import com.mooncloak.vpn.api.shared.plan.ServicePlansRepository
import com.mooncloak.vpn.app.android.activity.BaseActivity
import com.mooncloak.vpn.app.android.play.GooglePlayBillingManager
import com.mooncloak.vpn.app.android.play.ServicePlansGooglePlaySource
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.di.PresentationScoped
import com.mooncloak.vpn.app.shared.util.ActivityContext
import com.mooncloak.vpn.app.shared.util.ActivityForResultLauncher
import com.mooncloak.vpn.util.shared.coroutine.PresentationCoroutineScope

@Component
@PresentationScoped
internal abstract class AndroidGooglePlayPresentationComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @get:Provides override val presentationCoroutineScope: PresentationCoroutineScope,
    @get:Provides override val uriHandler: UriHandler,
    @get:Provides override val activityContext: ActivityContext,
    @get:Provides override val activity: ComponentActivity,
    @get:Provides override val activityForResultLauncher: ActivityForResultLauncher
) : AndroidPresentationComponent() {

    @Provides
    @PresentationScoped
    internal fun provideBillingManager(manager: GooglePlayBillingManager): BillingManager = manager

    @Provides
    @PresentationScoped
    internal fun providePlansRepository(source: ServicePlansGooglePlaySource): ServicePlansRepository = source
}

internal fun PresentationComponent.Companion.create(
    applicationComponent: ApplicationComponent,
    coroutineScope: PresentationCoroutineScope,
    activity: BaseActivity,
    uriHandler: UriHandler
): AndroidPresentationComponent = AndroidGooglePlayPresentationComponent::class.create(
    applicationComponent = applicationComponent,
    presentationCoroutineScope = coroutineScope,
    activityContext = activity,
    activity = activity,
    uriHandler = uriHandler,
    activityForResultLauncher = activity
)
