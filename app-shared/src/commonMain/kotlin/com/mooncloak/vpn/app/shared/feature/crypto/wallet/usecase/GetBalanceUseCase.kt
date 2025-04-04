package com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.locale.ExperimentalLocaleApi
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.FormattedCurrencyAmount
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.WalletBalance
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.util.shared.currency.Default

public class GetBalanceUseCase @Inject public constructor(
    private val cryptoWalletManager: CryptoWalletManager,
    private val currencyFormatter: Currency.Formatter = Currency.Formatter.Default,
    private val vpnServiceApi: VpnServiceApi
) {

    @OptIn(ExperimentalLocaleApi::class)
    public suspend operator fun invoke(address: String): WalletBalance {
        val balanceAmount = cryptoWalletManager.getBalance(address = address)
        val formattedBalance = currencyFormatter.format(amount = balanceAmount)

        return WalletBalance(
            amount = FormattedCurrencyAmount(
                amount = balanceAmount,
                formatted = formattedBalance
            ),
            // TODO: Get the estimated exchange value for the local fiat currency.
        )
    }
}
