package com.mooncloak.vpn.app.shared.api.wireguard

import com.mooncloak.vpn.api.shared.key.WireGuardConnectionKeyManager
import com.mooncloak.vpn.api.shared.key.WireGuardConnectionKeyPair

public operator fun WireGuardConnectionKeyManager.Companion.invoke(): WireGuardConnectionKeyManager =
    IosWireGuardConnectionKeyManager()

internal class IosWireGuardConnectionKeyManager internal constructor() : WireGuardConnectionKeyManager {

    override suspend fun generate(): WireGuardConnectionKeyPair = TODO("Not yet implemented")

    override suspend fun get(): WireGuardConnectionKeyPair? {
        TODO("Not yet implemented")
    }

    override suspend fun store(material: WireGuardConnectionKeyPair) {
        TODO("Not yet implemented")
    }

    internal companion object {

    }
}
