package com.mooncloak.vpn.app.shared.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.vpn.api.shared.billing.BillingManager
import com.mooncloak.vpn.api.shared.plan.ServicePlansProvider
import com.mooncloak.vpn.api.shared.plan.ServicePlansRepository
import com.mooncloak.vpn.api.shared.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider
import com.mooncloak.vpn.util.shared.coroutine.PresentationCoroutineScope
import com.mooncloak.vpn.util.shortcuts.AppShortcutProvider

public actual interface PresentationDependencies {

    public actual val uriHandler: UriHandler
    public actual val billingManager: BillingManager
    public actual val systemAuthenticationProvider: SystemAuthenticationProvider
    public actual val plansRepository: ServicePlansRepository
    public actual val plansProvider: ServicePlansProvider
    public actual val vpnConnectionManager: VPNConnectionManager
    public actual val presentationCoroutineScope: PresentationCoroutineScope
    public actual val libsLoader: LibsLoader
    public actual val appShortcutProvider: AppShortcutProvider

    public actual companion object
}
