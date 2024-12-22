package com.mooncloak.vpn.app.shared.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.vpn.app.shared.api.billing.BillingManager

public expect interface PresentationDependencies {

    public val uriHandler: UriHandler
    public val billingManager: BillingManager

    public companion object
}
