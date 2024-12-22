package com.mooncloak.vpn.app.android.di

import android.app.Activity
import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.di.PresentationScoped
import com.mooncloak.vpn.app.shared.util.ActivityContext

@Component
@PresentationScoped
internal abstract class AndroidPresentationComponent internal constructor(
    val applicationComponent: ApplicationComponent,
    val uriHandler: UriHandler,
    val activityContext: ActivityContext,
    val activity: Activity
) : PresentationComponent()

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
