package com.mooncloak.vpn.app.shared.di

import android.app.Activity
import androidx.compose.ui.platform.UriHandler
import com.mooncloak.vpn.app.shared.api.billing.BillingManager
import com.mooncloak.vpn.app.shared.api.plan.VPNServicePlansProvider
import com.mooncloak.vpn.app.shared.api.plan.VPNServicePlansRepository
import com.mooncloak.vpn.app.shared.util.ActivityContext
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider

public actual interface PresentationDependencies {

    public actual val uriHandler: UriHandler
    public actual val billingManager: BillingManager
    public actual val systemAuthenticationProvider: SystemAuthenticationProvider
    public actual val plansRepository: VPNServicePlansRepository
    public actual val plansProvider: VPNServicePlansProvider
    public val activityContext: ActivityContext
    public val activity: Activity

    public actual companion object
}
