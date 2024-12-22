package com.mooncloak.vpn.app.shared.di

import android.app.Activity
import androidx.compose.ui.platform.UriHandler
import com.mooncloak.vpn.app.shared.api.billing.BillingManager
import com.mooncloak.vpn.app.shared.util.ActivityContext

public actual interface PresentationDependencies {

    public actual val uriHandler: UriHandler
    public actual val billingManager: BillingManager
    public val activityContext: ActivityContext
    public val activity: Activity

    public actual companion object
}
