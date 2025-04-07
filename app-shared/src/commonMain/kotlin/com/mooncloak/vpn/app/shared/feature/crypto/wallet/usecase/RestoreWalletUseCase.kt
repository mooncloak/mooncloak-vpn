package com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.crypto.lunaris.CryptoPasswordManager
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.crypto.lunaris.getCurrentOrGenerate
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet

public class RestoreWalletUseCase @Inject public constructor(
    private val cryptoWalletManager: CryptoWalletManager,
    private val cryptoPasswordManager: CryptoPasswordManager
) {

    public suspend operator fun invoke(seedPhrase: String): CryptoWallet {
        val password = cryptoPasswordManager.getCurrentOrGenerate()

        return cryptoWalletManager.restoreWallet(phrase = seedPhrase, password = password)
    }
}
