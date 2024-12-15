package com.mooncloak.vpn.app.shared.di

import android.app.Activity
import androidx.compose.ui.platform.UriHandler
import com.mooncloak.vpn.app.shared.util.ActivityContext

public actual interface PresentationDependencies : ApplicationDependencies {

    public actual val uriHandler: UriHandler

    public val activityContext: ActivityContext
    public val activity: Activity

    public actual companion object
}
