package com.mooncloak.vpn.app.shared.feature.crypto.wallet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.locale.ExperimentalLocaleApi
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.currency.Currency
import com.mooncloak.vpn.api.shared.currency.Default
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.PromoDetails
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

                try {
                    emit { current -> current.copy(isLoading = true) }

                    blockChain = getString(Res.string.crypto_wallet_value_blockchain_ethereum)
                    network = getString(Res.string.crypto_wallet_value_network_polygon)
                    wallet = cryptoWalletManager.getDefaultWallet()
                    promoDetails = getPromoDetails(wallet = wallet)
                    timestamp = clock.now()

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            blockChain = blockChain,
                            network = network,
                            wallet = wallet,
                            promo = promoDetails,
                            timestamp = timestamp
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
        // TODO
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
}
