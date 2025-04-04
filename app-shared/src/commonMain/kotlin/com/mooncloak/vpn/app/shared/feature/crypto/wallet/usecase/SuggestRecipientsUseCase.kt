package com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.crypto.lunaris.model.CryptoAccount

public class SuggestRecipientsUseCase @Inject public constructor(
    private val cryptoWalletManager: CryptoWalletManager
) {

    public suspend operator fun invoke(value: String): List<CryptoAccount> {
        val recipient = cryptoWalletManager.resolveRecipient(value = value)

        return listOfNotNull(recipient)
    }
}
