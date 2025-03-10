package com.mooncloak.vpn.app.shared.api.key

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.key.WireGuardConnectionKeyManager
import com.mooncloak.vpn.api.shared.key.WireGuardConnectionKeyPair

public class WireGuardConnectionKeyPairResolver @Inject public constructor(
    private val keyManager: WireGuardConnectionKeyManager
) {

    public suspend fun resolve(): WireGuardConnectionKeyPair {
        var keyPair = keyManager.get()

        if (keyPair == null) {
            keyPair = keyManager.generate()

            keyManager.store(keyPair)
        }

        return keyPair
    }
}
