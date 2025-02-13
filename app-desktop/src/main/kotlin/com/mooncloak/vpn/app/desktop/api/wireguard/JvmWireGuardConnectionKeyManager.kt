package com.mooncloak.vpn.app.desktop.api.wireguard

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.key.WireGuardConnectionKeyManager
import com.mooncloak.vpn.app.shared.api.key.WireGuardConnectionKeyPair

internal class JvmWireGuardConnectionKeyManager @Inject internal constructor() : WireGuardConnectionKeyManager {

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
