package com.mooncloak.vpn.app.android.di

import android.app.Activity
import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.di.PresentationScoped
import com.mooncloak.vpn.app.shared.util.ActivityContext

@Component
@PresentationScoped
internal abstract class AndroidPresentationComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @get:Provides override val uriHandler: UriHandler,
    @get:Provides override val activityContext: ActivityContext,
    @get:Provides override val activity: Activity
) : PresentationComponent()

internal fun PresentationComponent.Companion.create(
    applicationComponent: ApplicationComponent,
    activity: Activity,
    uriHandler: UriHandler
): AndroidPresentationComponent = AndroidPresentationComponent::class.create(
    applicationComponent = applicationComponent,
    uriHandler = uriHandler,
    activityContext = activity,
    activity = activity
)
