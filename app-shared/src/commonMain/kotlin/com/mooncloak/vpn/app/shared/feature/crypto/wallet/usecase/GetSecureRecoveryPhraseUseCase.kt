package com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.crypto.lunaris.CryptoPasswordManager
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.crypto.lunaris.getCurrentOrGenerate

public class GetSecureRecoveryPhraseUseCase @Inject public constructor(
    private val cryptoWalletManager: CryptoWalletManager,
    private val cryptoPasswordManager: CryptoPasswordManager
) {

    public suspend operator fun invoke(address: String): String? {
        val password = cryptoPasswordManager.getCurrentOrGenerate()

        return cryptoWalletManager.revealSeedPhrase(address = address, password = password)
    }
}
