package com.mooncloak.vpn.crypto.lunaris.provider

import com.mooncloak.vpn.data.shared.provider.Provider

/**
 * Provides the address of the current Crypto Wallet instance. This allows the user to select their current wallet and
 * maintain multiple wallets.
 */
public fun interface CryptoWalletAddressProvider : Provider<String> {

    public companion object
}
