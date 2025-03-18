package com.mooncloak.vpn.app.shared.api.wireguard

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.key.WireGuardConnectionKeyManager
import com.mooncloak.vpn.api.shared.key.WireGuardConnectionKeyPair

internal class IosWireGuardConnectionKeyManager @Inject internal constructor() : WireGuardConnectionKeyManager {

    override suspend fun generate(): WireGuardConnectionKeyPair {
        TODO("Not yet implemented")
    }

    override suspend fun store(material: WireGuardConnectionKeyPair) {
        TODO("Not yet implemented")
    }

    override suspend fun get(): WireGuardConnectionKeyPair? {
        TODO("Not yet implemented")
    }
}
