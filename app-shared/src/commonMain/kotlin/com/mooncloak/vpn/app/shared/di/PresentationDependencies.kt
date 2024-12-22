package com.mooncloak.vpn.app.shared.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.vpn.app.shared.api.billing.BillingManager
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider

public expect interface PresentationDependencies {

    public val uriHandler: UriHandler
    public val billingManager: BillingManager
    public val systemAuthenticationProvider: SystemAuthenticationProvider

    public companion object
}
