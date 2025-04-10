package com.mooncloak.vpn.app.shared.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.vpn.api.shared.billing.IosBillingManager
import com.mooncloak.vpn.util.shared.coroutine.PresentationCoroutineScope

internal actual fun PresentationComponent.Companion.create(
    applicationComponent: ApplicationComponent,
    presentationCoroutineScope: PresentationCoroutineScope,
    uriHandler: UriHandler,
    billingManagerFactory: IosBillingManager.Factory
): IosPresentationComponent = IosPresentationComponent::class.create(
    applicationComponent = applicationComponent,
    presentationCoroutineScope = presentationCoroutineScope,
    uriHandler = uriHandler,
    billingManagerFactory = billingManagerFactory
)
