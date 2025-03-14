package com.mooncloak.vpn.app.shared.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.vpn.api.shared.billing.BillingManager
import com.mooncloak.vpn.api.shared.plan.ServicePlansProvider
import com.mooncloak.vpn.api.shared.plan.ServicePlansRepository
import com.mooncloak.vpn.network.core.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider
import com.mooncloak.vpn.util.shared.coroutine.PresentationCoroutineScope
import com.mooncloak.vpn.util.shortcuts.AppShortcutProvider

public expect interface PresentationDependencies {

    public val uriHandler: UriHandler
    public val billingManager: BillingManager
    public val systemAuthenticationProvider: SystemAuthenticationProvider
    public val plansRepository: ServicePlansRepository
    public val plansProvider: ServicePlansProvider
    public val vpnConnectionManager: VPNConnectionManager
    public val presentationCoroutineScope: PresentationCoroutineScope
    public val libsLoader: LibsLoader
    public val appShortcutProvider: AppShortcutProvider

    public companion object
}
