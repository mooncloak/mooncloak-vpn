package com.mooncloak.vpn.app.shared.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.vpn.app.shared.api.billing.BillingManager
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansProvider
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansRepository
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider

public actual interface PresentationDependencies {

    public actual val uriHandler: UriHandler
    public actual val billingManager: BillingManager
    public actual val systemAuthenticationProvider: SystemAuthenticationProvider
    public actual val plansRepository: ServicePlansRepository
    public actual val plansProvider: ServicePlansProvider

    public actual companion object
}
