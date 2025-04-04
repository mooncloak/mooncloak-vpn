package com.mooncloak.vpn.app.shared.feature.crypto.wallet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.TextFieldValue
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.locale.ExperimentalLocaleApi
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.currency.Currency
import com.mooncloak.vpn.api.shared.currency.Default
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.PromoDetails
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.WalletBalance
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.WalletFeedItem
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.WalletStatDetails
import com.mooncloak.vpn.app.shared.model.NotificationStateModel
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_promo_description
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_promo_description_gifted
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_promo_title
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_promo_title_gifted
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_value_blockchain_ethereum
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_value_network_polygon
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet
import com.mooncloak.vpn.crypto.lunaris.repository.GiftedCryptoTokenRepository
import com.mooncloak.vpn.util.shared.time.DateTimeFormatter
import com.mooncloak.vpn.util.shared.time.Full
import com.mooncloak.vpn.util.shared.time.format
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.jetbrains.compose.resources.getString

@Stable
public class CryptoWalletViewModel @Inject public constructor(
    private val cryptoWalletManager: CryptoWalletManager,
    private val vpnServiceApi: VpnServiceApi,
    private val clock: Clock,
    private val giftedCryptoTokenRepository: GiftedCryptoTokenRepository,
    private val currencyFormatter: Currency.Formatter = Currency.Formatter.Default,
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.Full
) : ViewModel<CryptoWalletStateModel>(initialStateValue = CryptoWalletStateModel()) {

    private val mutex = Mutex(locked = false)

    public fun load() {
        coroutineScope.launch {
            mutex.withLock {
                var blockChain: String? = null
                var network: String? = null
                var wallet: CryptoWallet? = null
                var promoDetails: PromoDetails? = null
                var timestamp: Instant? = null
                var items = emptyList<WalletFeedItem>()

                try {
                    emit { current -> current.copy(isLoading = true) }

                    blockChain = getString(Res.string.crypto_wallet_value_blockchain_ethereum)
                    network = getString(Res.string.crypto_wallet_value_network_polygon)
                    wallet = cryptoWalletManager.getDefaultWallet()
                    promoDetails = getPromoDetails(wallet = wallet)
                    timestamp = clock.now()
                    items = getFeedItems(
                        wallet = wallet,
                        balance = null,
                        statDetails = null,
                        promoDetails = promoDetails,
                        blockChain = blockChain,
                        network = network,
                        timestamp = dateTimeFormatter.format(timestamp)
                    )

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            blockChain = blockChain,
                            network = network,
                            wallet = wallet,
                            promo = promoDetails,
                            timestamp = timestamp,
                            items = items
                        )
                    }
                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error loading Lunaris Wallet.",
                        cause = e
                    )

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            blockChain = blockChain,
                            network = network,
                            wallet = wallet,
                            promo = promoDetails,
                            timestamp = timestamp,
                            items = items,
                            error = NotificationStateModel(
                                message = getString(Res.string.global_unexpected_error)
                            )
                        )
                    }
                }
            }
        }
    }

    public fun refresh() {
        load()
    }

    public fun updateAddress(value: TextFieldValue) {
        coroutineScope.launch {
            mutex.withLock {
                try {

                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error updating address.",
                        cause = e
                    )
                }
            }
        }
    }

    public fun updateAmount(value: TextFieldValue) {
        coroutineScope.launch {
            mutex.withLock {
                try {

                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error updating amount.",
                        cause = e
                    )
                }
            }
        }
    }

    public fun sendPayment() {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    // TODO
                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error sending payment.",
                        cause = e
                    )


                }
            }
        }
    }

    @OptIn(ExperimentalLocaleApi::class)
    private suspend fun getPromoDetails(wallet: CryptoWallet?): PromoDetails? {
        val now = clock.now()

        // Arbitrary cut-off of May 5th, 2025
        val expiration = LocalDate(year = 2025, month = Month.MAY, dayOfMonth = 5)
            .atStartOfDayIn(timeZone = TimeZone.currentSystemDefault())

        if (wallet != null) {
            val stats = giftedCryptoTokenRepository.getStats()

            if (stats != null) {
                val amount = currencyFormatter.format(
                    currency = stats.total.currency,
                    amount = stats.total.toMinorUnits()
                )
                val time = dateTimeFormatter.format(stats.lastGifted)

                return PromoDetails(
                    title = getString(Res.string.crypto_wallet_promo_title_gifted, amount),
                    description = getString(Res.string.crypto_wallet_promo_description_gifted, time),
                    icon = { rememberVectorPainter(Icons.Default.CardGiftcard) }
                )
            }
        }

        return when {
            wallet != null -> null // TODO: Check if lunaris was gifted.
            now < expiration -> PromoDetails(
                title = getString(Res.string.crypto_wallet_promo_title),
                description = getString(Res.string.crypto_wallet_promo_description),
                icon = { rememberVectorPainter(Icons.Default.CardGiftcard) }
            )

            else -> null
        }
    }

    private fun getFeedItems(
        wallet: CryptoWallet?,
        balance: WalletBalance?,
        statDetails: WalletStatDetails?,
        promoDetails: PromoDetails?,
        blockChain: String?,
        network: String?,
        timestamp: String?
    ): List<WalletFeedItem> {
        val items = mutableListOf<WalletFeedItem>()

        if (balance != null) {
            items.add(
                WalletFeedItem.BalanceItem(balance = balance)
            )
        }

        if (promoDetails != null) {
            items.add(WalletFeedItem.PromoItem(details = promoDetails))
        }

        if (wallet == null) {
            items.add(WalletFeedItem.NoWalletItem)
        }

        if (statDetails != null) {
            items.add(WalletFeedItem.StatsItem(stats = statDetails))
        }

        items.add(WalletFeedItem.ActionsItem)

        if (wallet != null) {
            items.add(WalletFeedItem.AccountAddressItem(wallet = wallet))
        }

        items.add(
            WalletFeedItem.DetailsItem(
                wallet = wallet,
                balance = balance,
                blockChain = blockChain,
                network = network,
                timestamp = timestamp
            )
        )

        return items
    }
}
