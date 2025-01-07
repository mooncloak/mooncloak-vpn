package com.mooncloak.vpn.app.shared.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.vpn.app.shared.api.billing.BillingManager
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansProvider
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansRepository
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider
import com.mooncloak.vpn.app.shared.util.coroutine.PresentationCoroutineScope
import com.mooncloak.vpn.app.shared.util.notification.NotificationManager

public expect interface PresentationDependencies {

    public val uriHandler: UriHandler
    public val billingManager: BillingManager
    public val systemAuthenticationProvider: SystemAuthenticationProvider
    public val plansRepository: ServicePlansRepository
    public val plansProvider: ServicePlansProvider
    public val vpnConnectionManager: VPNConnectionManager
    public val presentationCoroutineScope: PresentationCoroutineScope
    public val notificationManager: NotificationManager

    public companion object
}
