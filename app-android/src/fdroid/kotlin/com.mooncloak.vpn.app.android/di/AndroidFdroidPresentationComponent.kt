package com.mooncloak.vpn.app.android.di

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.api.shared.billing.BillingManager
import com.mooncloak.vpn.api.shared.plan.ServicePlansRepository
import com.mooncloak.vpn.app.shared.api.billing.MooncloakBillingManager
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansApiSource
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.di.PresentationScoped
import com.mooncloak.vpn.app.shared.util.ActivityContext
import com.mooncloak.vpn.util.shared.coroutine.PresentationCoroutineScope

@Component
@PresentationScoped
internal abstract class AndroidFdroidPresentationComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @get:Provides override val presentationCoroutineScope: PresentationCoroutineScope,
    @get:Provides override val uriHandler: UriHandler,
    @get:Provides override val activityContext: ActivityContext,
    @get:Provides override val activity: ComponentActivity
) : AndroidPresentationComponent() {

    @Provides
    @PresentationScoped
    internal fun provideBillingManager(manager: MooncloakBillingManager): BillingManager = manager

    @Provides
    @PresentationScoped
    internal fun providePlansRepository(source: ServicePlansApiSource): ServicePlansRepository = source
}

internal fun PresentationComponent.Companion.create(
    applicationComponent: ApplicationComponent,
    coroutineScope: PresentationCoroutineScope,
    activity: ComponentActivity,
    uriHandler: UriHandler
): AndroidPresentationComponent = AndroidFdroidPresentationComponent::class.create(
    applicationComponent = applicationComponent,
    presentationCoroutineScope = coroutineScope,
    activityContext = activity,
    activity = activity,
    uriHandler = uriHandler
)
