package com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.crypto.lunaris.model.CryptoAddress
import com.mooncloak.vpn.crypto.lunaris.provider.CryptoRecipientAddressProvider

public class SuggestRecipientsUseCase @Inject public constructor(
    private val cryptoRecipientAddressProvider: CryptoRecipientAddressProvider
) {

    public suspend operator fun invoke(value: String): List<CryptoAddress> =
        cryptoRecipientAddressProvider.search(value = value)
}
