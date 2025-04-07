package com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.locale.ExperimentalLocaleApi
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.warning
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.FormattedCurrencyAmount
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.WalletBalance
import com.mooncloak.vpn.app.shared.settings.SubscriptionSettings
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.util.shared.currency.Default
import com.mooncloak.vpn.util.shared.currency.USD

public class GetBalanceUseCase @Inject public constructor(
    private val cryptoWalletManager: CryptoWalletManager,
    private val currencyFormatter: Currency.Formatter = Currency.Formatter.Default,
    private val vpnServiceApi: VpnServiceApi,
    private val subscriptionSettings: SubscriptionSettings
) {

    @OptIn(ExperimentalLocaleApi::class)
    public suspend operator fun invoke(address: String): WalletBalance {
        val balanceAmount = cryptoWalletManager.getBalance(address = address)
        val formattedBalance = currencyFormatter.format(amount = balanceAmount)
        val cryptoAmount = FormattedCurrencyAmount(
            amount = balanceAmount,
            formatted = formattedBalance
        )

        val estimatedFiat = try {
            val exchangeAmount = vpnServiceApi.exchangeCurrency(
                token = subscriptionSettings.tokens.get()?.accessToken,
                amount = balanceAmount,
                target = Currency.USD
            ).target
            val formattedExchangeAmount = currencyFormatter.format(amount = exchangeAmount)

            FormattedCurrencyAmount(
                amount = exchangeAmount,
                formatted = formattedExchangeAmount
            )
        } catch (e: Exception) {
            LogPile.warning(
                message = "Error retrieving estimated fiat.",
                cause = e
            )

            null
        }

        return WalletBalance(
            amount = cryptoAmount,
            localEstimate = estimatedFiat
        )
    }
}
