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
    ).first() as NSString

    // Construct the wallet directory path (e.g., Documents/mooncloak/wallets)
    @Suppress("CAST_NEVER_SUCCEEDS") // Required for Objective-C compatibility for this function to cast DEFAULT_WALLET_DIRECTORY_NAME as an NSString instance.
    return NSString.stringWithFormat(
        "%@/mooncloak/%@",
        documentsDir,
        DEFAULT_WALLET_DIRECTORY_NAME as NSString
    )
}
