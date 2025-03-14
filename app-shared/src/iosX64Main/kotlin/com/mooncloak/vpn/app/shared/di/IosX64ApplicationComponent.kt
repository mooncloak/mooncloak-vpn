package com.mooncloak.vpn.app.shared.di

import com.mooncloak.vpn.app.shared.api.wireguard.IosWireGuardConnectionKeyManager
import com.mooncloak.vpn.app.shared.api.wireguard.IosWireGuardTunnelManager
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope

internal actual fun ApplicationComponent.Companion.create(
    applicationCoroutineScope: ApplicationCoroutineScope,
    wireGuardConnectionKeyManager: IosWireGuardConnectionKeyManager,
    wireGuardTunnelManager: IosWireGuardTunnelManager
): IosApplicationComponent = IosApplicationComponent::class.create(
    applicationCoroutineScope = applicationCoroutineScope,
    wireGuardConnectionKeyManager = wireGuardConnectionKeyManager,
    wireGuardTunnelManager = wireGuardTunnelManager
)
