package com.mooncloak.vpn.app.shared.di

import com.mooncloak.vpn.app.shared.crypto.IosCryptoWalletManager
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope

internal actual fun ApplicationComponent.Companion.create(
    applicationCoroutineScope: ApplicationCoroutineScope,
    cryptoWalletManagerFactory: IosCryptoWalletManager.Factory
): IosApplicationComponent = IosApplicationComponent::class.create(
    applicationCoroutineScope = applicationCoroutineScope,
    cryptoWalletManagerFactory = cryptoWalletManagerFactory
)
