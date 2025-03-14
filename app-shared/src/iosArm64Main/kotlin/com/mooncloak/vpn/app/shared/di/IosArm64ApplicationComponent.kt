package com.mooncloak.vpn.app.shared.di

import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope

internal fun ApplicationComponent.Companion.create(
    applicationCoroutineScope: ApplicationCoroutineScope
): IosApplicationComponent = IosApplicationComponent::class.create(
    applicationCoroutineScope = applicationCoroutineScope
)
