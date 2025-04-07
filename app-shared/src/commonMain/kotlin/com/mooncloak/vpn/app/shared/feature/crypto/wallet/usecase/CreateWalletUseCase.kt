package com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.warning
import com.mooncloak.vpn.crypto.lunaris.CryptoPasswordManager
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.crypto.lunaris.getCurrentOrGenerate
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet

public class CreateWalletUseCase @Inject public constructor(
    private val cryptoWalletManager: CryptoWalletManager,
    private val cryptoPasswordManager: CryptoPasswordManager,
    private val requestLunarisGift: RequestLunarisGiftUseCase
) {

    public suspend operator fun invoke(): CryptoWallet {
        val password = cryptoPasswordManager.getCurrentOrGenerate()

        val wallet = cryptoWalletManager.createWallet(password = password)

        try {
            // Request Lunaris tokens. These will be stored by the use case, so they can be retrieved later if needed.
            // Don't crash here on failure because the error is expected when the promotion ends.
            requestLunarisGift(address = wallet.address)
        } catch (e: Exception) {
            LogPile.warning(
                message = "Error requesting Lunaris gift.",
                cause = e
            )
        }

        return wallet
    }
}
