package com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.crypto.lunaris.CryptoPasswordManager
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.crypto.lunaris.getCurrentOrGenerate
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet

public class CreateWalletUseCase @Inject public constructor(
    private val cryptoWalletManager: CryptoWalletManager,
    private val cryptoPasswordManager: CryptoPasswordManager
) {

    public suspend operator fun invoke(): CryptoWallet {
        val password = cryptoPasswordManager.getCurrentOrGenerate()

        return cryptoWalletManager.createWallet(password = password)
    }
}
