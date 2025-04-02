package com.mooncloak.vpn.app.shared.settings

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.data.shared.keyvalue.MutableKeyValueProperty
import com.mooncloak.vpn.data.shared.keyvalue.MutableKeyValueStorage
import com.mooncloak.vpn.data.shared.keyvalue.property

@Singleton
public class CryptoSettings @Inject public constructor(
    storage: MutableKeyValueStorage
) {

    public val walletAddress: MutableKeyValueProperty<String> by storage.property(key = WALLET_ADDRESS)

    internal companion object Key {

        private const val WALLET_ADDRESS = "com.mooncloak.vpn.app.storage.key.crypto.wallet_address"
    }
}
