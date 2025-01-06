package com.mooncloak.vpn.app.shared.api.key

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public class WireGuardConnectionKeyPairResolver @Inject public constructor(
    private val keyManager: WireGuardConnectionKeyManager,
    private val api: MooncloakVpnServiceHttpApi,
    private val subscriptionStorage: SubscriptionStorage
) {

    @OptIn(ExperimentalPersistentStateAPI::class)
    public suspend fun resolve(): WireGuardConnectionKeyPair {
        var keyPair = keyManager.get()

        if (keyPair == null) {
            keyPair = keyManager.generate()

            withContext(Dispatchers.IO){
                api.registerClient(
                    clientPublicKey = keyPair.publicKey,
                    token = subscriptionStorage.tokens.current.value?.accessToken
                )
            }

            keyManager.store(keyPair)
        }

        return keyPair
    }
}
