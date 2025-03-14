package com.mooncloak.vpn.app.shared.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.vpn.util.shared.coroutine.PresentationCoroutineScope

internal fun PresentationComponent.Companion.create(
    applicationComponent: ApplicationComponent,
    presentationCoroutineScope: PresentationCoroutineScope,
    uriHandler: UriHandler
): IosPresentationComponent = IosPresentationComponent::class.create(
    applicationComponent = applicationComponent,
    presentationCoroutineScope = presentationCoroutineScope,
    uriHandler = uriHandler
)
