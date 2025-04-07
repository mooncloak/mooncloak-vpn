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
import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.util.shared.currency.Default
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.PromoDetails
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.WalletBalance
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.WalletFeedItem
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.WalletStatDetails
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase.CreateWalletUseCase
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase.GetBalanceUseCase
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase.GetSecureRecoveryPhraseUseCase
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase.RestoreWalletUseCase
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase.SendLunarisUseCase
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase.SuggestRecipientsUseCase
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.validation.LunarisAddressValidator
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.validation.SecretRecoveryPhraseValidationException
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.validation.SecretRecoveryPhraseValidator
import com.mooncloak.vpn.app.shared.model.NotificationStateModel
import com.mooncloak.vpn.app.shared.model.TextFieldStateModel
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_error_amount_invalid
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_error_phrase_invalid
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_error_phrase_invalid_word_count
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_estimated_gas
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_promo_description
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_promo_description_gifted
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_promo_title
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_promo_title_gifted
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_title_authenticate_recovery
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_value_blockchain_ethereum
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_value_network_polygon
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider
import com.mooncloak.vpn.app.shared.util.isSuccess
import com.mooncloak.vpn.app.shared.util.launchAuthentication
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet
import com.mooncloak.vpn.crypto.lunaris.repository.GiftedCryptoTokenRepository
import com.mooncloak.vpn.util.shared.currency.lunarisValidator
import com.mooncloak.vpn.util.shared.time.DateTimeFormatter
import com.mooncloak.vpn.util.shared.time.Full
import com.mooncloak.vpn.util.shared.time.format
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
import kotlin.time.Duration.Companion.milliseconds

@Stable
public class CryptoWalletViewModel @Inject public constructor(
    private val cryptoWalletManager: CryptoWalletManager,
    private val clock: Clock,
    private val giftedCryptoTokenRepository: GiftedCryptoTokenRepository,
    private val currencyFormatter: Currency.Formatter = Currency.Formatter.Default,
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.Full,
    private val secretRecoveryPhraseValidator: SecretRecoveryPhraseValidator,
    private val suggestRecipients: SuggestRecipientsUseCase,
    private val createNewWallet: CreateWalletUseCase,
    private val restoreExistingWallet: RestoreWalletUseCase,
    private val getBalance: GetBalanceUseCase,
    private val getSecureRecoveryPhrase: GetSecureRecoveryPhraseUseCase,
    private val sendLunaris: SendLunarisUseCase,
    private val systemAuthenticationProvider: SystemAuthenticationProvider
) : ViewModel<CryptoWalletStateModel>(initialStateValue = CryptoWalletStateModel()) {

    private val mutex = Mutex(locked = false)

    @OptIn(ExperimentalLocaleApi::class)
    private val lunarisCurrencyAmountValidator = Currency.Amount.lunarisValidator()
    private val lunarisAddressValidator = LunarisAddressValidator()

    private val addressState = MutableStateFlow(TextFieldValue())
    private val amountState = MutableStateFlow(TextFieldValue())
    private val phraseState = MutableStateFlow(TextFieldValue())

    private var addressJob: Job? = null
    private var amountJob: Job? = null
    private var phraseJob: Job? = null

    public fun load() {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    emit { current -> current.copy(isLoading = true) }

                    subscribeToAddressChanges()
                    subscribeToAmountChanges()
                    subscribeToPhraseChanges()

                    val wallet = cryptoWalletManager.getDefaultWallet()

                    loadWalletDataAndEmit(wallet = wallet)
                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error loading Lunaris Wallet.",
                        cause = e
                    )

                    emit { current ->
                        current.copy(
                            isLoading = false,
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
                addressState.value = value
            }
        }
    }

    public fun updateAmount(value: TextFieldValue) {
        coroutineScope.launch {
            mutex.withLock {
                amountState.value = value
            }
        }
    }

    public fun updatePhrase(value: TextFieldValue) {
        coroutineScope.launch {
            mutex.withLock {
                phraseState.value = value
            }
        }
    }

    public fun togglePhraseVisibility() {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    val isCurrentlyVisible = state.current.value.secureRecoveryPhraseVisible

                    // We are toggling to visible, and system authentication is supported, so require authentication.
                    if (!isCurrentlyVisible && systemAuthenticationProvider.isSupported) {
                        val result = systemAuthenticationProvider.launchAuthentication(
                            title = getString(Res.string.crypto_wallet_title_authenticate_recovery),
                            subtitle = null,
                            description = null
                        )

                        // Only toggle to visible if authentication was successful.
                        if (result.isSuccess()) {
                            emit { current -> current.copy(secureRecoveryPhraseVisible = true) }
                        }
                    } else {
                        emit { current -> current.copy(secureRecoveryPhraseVisible = !isCurrentlyVisible) }
                    }
                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error toggling visibility of secure recovery phrase.",
                        cause = e
                    )

                    emit { current ->
                        current.copy(
                            error = NotificationStateModel(
                                message = getString(Res.string.global_unexpected_error)
                            )
                        )
                    }
                }
            }
        }
    }

    public fun sendPayment() {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    // TODO
                    emit { current -> current.copy(send = current.send.copy(isSending = true)) }

                    val currentState = state.current.value

                    sendLunaris(
                        origin = currentState.wallet?.address ?: error("Cannot send if wallet is null."),
                        target = currentState.send.address.value.text,
                        amount = currentState.send.amount.value.text
                    )
                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error sending payment.",
                        cause = e
                    )

                    emit { current ->
                        current.copy(
                            send = current.send.copy(
                                isSending = false
                            ),
                            error = NotificationStateModel(
                                message = getString(Res.string.global_unexpected_error)
                            )
                        )
                    }
                }
            }
        }
    }

    public fun createWallet() {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    emit { current -> current.copy(isCreatingWallet = true) }

                    val wallet = createNewWallet()

                    loadWalletDataAndEmit(wallet = wallet)
                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error creating wallet.",
                        cause = e
                    )

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            isCreatingWallet = false,
                            error = NotificationStateModel(
                                message = getString(Res.string.global_unexpected_error)
                            )
                        )
                    }
                }
            }
        }
    }

    public fun restoreWallet() {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    emit { current -> current.copy(restore = current.restore.copy(isRestoring = true)) }

                    val phrase = state.current.value.restore.phrase.value.text
                    val wallet = restoreExistingWallet(seedPhrase = phrase)

                    loadWalletDataAndEmit(wallet = wallet)
                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error creating wallet.",
                        cause = e
                    )

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            isCreatingWallet = false,
                            restore = current.restore.copy(isRestoring = false),
                            error = NotificationStateModel(
                                message = getString(Res.string.global_unexpected_error)
                            )
                        )
                    }
                }
            }
        }
    }

    private fun subscribeToAddressChanges() {
        addressJob?.cancel()
        addressJob = addressState.onEach { value ->
            // Update the address value quickly so that there is no lag as the user types.
            // Then load the extra data.
            val amountResult = lunarisAddressValidator.validate(value.text)
            val error = if (amountResult.isSuccess) {
                null
            } else {
                // TODO: Be more specific about the error message.
                getString(Res.string.crypto_wallet_error_amount_invalid)
            }

            emit { current ->
                current.copy(
                    send = current.send.copy(
                        address = TextFieldStateModel(
                            value = value,
                            error = error
                        )
                    )
                )
            }
        }.debounce(300.milliseconds)
            .onEach { value ->
                val suggestedRecipients = suggestRecipients.invoke(value = value.text)

                emit { current ->
                    current.copy(
                        send = current.send.copy(
                            suggestedRecipients = suggestedRecipients
                        )
                    )
                }
            }
            .launchIn(coroutineScope)
    }

    private fun subscribeToAmountChanges() {
        amountJob?.cancel()
        amountJob = amountState.map { value ->
            // Update the address value quickly so that there is no lag as the user types.
            // Then load the extra data.
            val amountResult = lunarisCurrencyAmountValidator.validate(value.text)
            val error = if (amountResult.isSuccess) {
                null
            } else {
                // TODO: Be more specific about the error message.
                getString(Res.string.crypto_wallet_error_amount_invalid)
            }

            emit { current ->
                current.copy(
                    send = current.send.copy(
                        amount = TextFieldStateModel(
                            value = value,
                            error = error
                        )
                    )
                )
            }

            amountResult
        }.debounce(300.milliseconds)
            .onEach { amountResult ->
                val amount = amountResult.getOrNull()

                val gas = if (amount != null) {
                    val currentState = state.current.value
                    val gasAmount = cryptoWalletManager.estimateGas(
                        origin = currentState.wallet?.address ?: "",
                        target = currentState.send.address.value.text,
                        amount = amount
                    )

                    gasAmount?.let { getString(Res.string.crypto_wallet_estimated_gas, it) }
                } else {
                    null
                }

                emit { current -> current.copy(send = current.send.copy(estimatedGas = gas)) }
            }
            .launchIn(coroutineScope)
    }

    private fun subscribeToPhraseChanges() {
        phraseJob?.cancel()
        phraseJob = phraseState.onEach { value ->
            // Update the address value quickly so that there is no lag as the user types.
            // Then load the extra data.
            val result = secretRecoveryPhraseValidator.validate(value.text)
            val exception = result.exceptionOrNull()
            val error = when (exception) {
                null -> null
                is SecretRecoveryPhraseValidationException.IncorrectWordCount -> getString(Res.string.crypto_wallet_error_phrase_invalid_word_count)
                else -> getString(Res.string.crypto_wallet_error_phrase_invalid)
            }

            emit { current ->
                current.copy(
                    restore = current.restore.copy(
                        phrase = TextFieldStateModel(
                            value = value,
                            error = error
                        )
                    )
                )
            }
        }.launchIn(coroutineScope)
    }

    private suspend fun loadWalletDataAndEmit(
        wallet: CryptoWallet?
    ) {
        val currentState = state.current.value

        var blockChain: String? = currentState.blockChain
        var network: String? = currentState.network
        var balance: WalletBalance? = currentState.balance
        var promoDetails: PromoDetails? = currentState.promo
        var timestamp: Instant? = currentState.timestamp
        var items = currentState.items
        var secureRecoveryPhrase: String? = currentState.secureRecoveryPhrase

        try {
            emit { current -> current.copy(isLoading = true) }

            blockChain = getString(Res.string.crypto_wallet_value_blockchain_ethereum)
            network = getString(Res.string.crypto_wallet_value_network_polygon)
            balance = wallet?.address?.let { getBalance.invoke(address = it) }
            promoDetails = getPromoDetails(wallet = wallet)
            timestamp = clock.now()
            items = getFeedItems(
                wallet = wallet,
                balance = balance,
                statDetails = null,
                promoDetails = promoDetails,
                blockChain = blockChain,
                network = network,
                timestamp = dateTimeFormatter.format(timestamp)
            )
            secureRecoveryPhrase = wallet?.address?.let { address ->
                getSecureRecoveryPhrase(address = address)
            }

            emit { current ->
                current.copy(
                    isLoading = false,
                    isCreatingWallet = false,
                    blockChain = blockChain,
                    network = network,
                    wallet = wallet,
                    balance = balance,
                    promo = promoDetails,
                    timestamp = timestamp,
                    items = items,
                    secureRecoveryPhrase = secureRecoveryPhrase
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
                    isCreatingWallet = false,
                    blockChain = blockChain,
                    network = network,
                    wallet = wallet,
                    balance = balance,
                    promo = promoDetails,
                    timestamp = timestamp,
                    items = items,
                    secureRecoveryPhrase = secureRecoveryPhrase,
                    error = NotificationStateModel(
                        message = getString(Res.string.global_unexpected_error)
                    )
                )
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
                    amount = stats.total
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
            wallet != null -> null

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
