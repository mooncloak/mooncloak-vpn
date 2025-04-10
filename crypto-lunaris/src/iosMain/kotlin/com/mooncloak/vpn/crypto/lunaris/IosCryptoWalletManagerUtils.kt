package com.mooncloak.vpn.crypto.lunaris

import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUserDomainMask
import platform.Foundation.stringWithFormat

public fun CryptoWalletManager.Companion.walletDirectory(): String {
    // Get the Documents directory on iOS
    val documentsDir = NSSearchPathForDirectoriesInDomains(
        NSDocumentDirectory,
        NSUserDomainMask,
        true
    ).first() as String

    // Construct the wallet directory path (e.g., Documents/mooncloak/wallets)
    return NSString.stringWithFormat("%@/mooncloak/%@", documentsDir, DEFAULT_WALLET_DIRECTORY_NAME)
}
