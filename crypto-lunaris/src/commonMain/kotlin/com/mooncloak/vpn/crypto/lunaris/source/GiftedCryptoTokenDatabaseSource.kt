package com.mooncloak.vpn.crypto.lunaris.source

import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.util.shared.currency.Lunaris
import com.mooncloak.vpn.util.shared.currency.invoke
import com.mooncloak.vpn.crypto.lunaris.model.GiftedCryptoToken
import com.mooncloak.vpn.crypto.lunaris.model.GiftedCryptoTokenStats
import com.mooncloak.vpn.crypto.lunaris.repository.GiftedCryptoTokenRepository
import com.mooncloak.vpn.data.sqlite.DatabaseProvider
import com.mooncloak.vpn.data.sqlite.database.Gifted_crypto_token
import com.mooncloak.vpn.data.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

public operator fun GiftedCryptoTokenRepository.Companion.invoke(
    databaseProvider: DatabaseProvider<MooncloakDatabase>
): GiftedCryptoTokenRepository = GiftedCryptoTokenDatabaseSource(
    databaseProvider = databaseProvider
)

internal class GiftedCryptoTokenDatabaseSource internal constructor(
    private val databaseProvider: DatabaseProvider<MooncloakDatabase>
) : GiftedCryptoTokenRepository {

    private val mutex = Mutex(locked = false)

    override suspend fun get(id: String): GiftedCryptoToken =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            database.giftedCryptoTokenQueries.selectById(id = id)
                .executeAsOneOrNull()
                ?.toGiftedCryptoToken()
                ?: throw NoSuchElementException("No '${GiftedCryptoToken::class.simpleName}' found with id '$id'.")
        }

    override suspend fun get(count: Int, offset: Int): List<GiftedCryptoToken> =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            database.giftedCryptoTokenQueries.selectPage(
                limit = count.toLong(),
                offset = offset.toLong()
            ).executeAsList()
                .map { it.toGiftedCryptoToken() }
        }

    override suspend fun getAll(): List<GiftedCryptoToken> =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            database.giftedCryptoTokenQueries.selectAll()
                .executeAsList()
                .map { it.toGiftedCryptoToken() }
        }

    override suspend fun getStats(): GiftedCryptoTokenStats? =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            // FIXME: This query assumes all the gifted tokens are in the same currency and format.
            val result = database.giftedCryptoTokenQueries.selectLastGiftedAndTotal()
                .executeAsOneOrNull()
            val lastGifted = result?.lastGifted
            val totalAmountGifted = result?.totalAmountGifted

            return@withContext if (lastGifted != null && totalAmountGifted != null) {
                GiftedCryptoTokenStats(
                    lastGifted = lastGifted,
                    total = Currency.Amount(
                        currency = Currency.Lunaris, // FIXME: Hardcoded use of Lunaris
                        unit = Currency.Unit.Minor, // FIXME: Hardcoded use of Currency.Unit.Minor
                        value = totalAmountGifted
                    )
                )
            } else {
                null
            }
        }

    override suspend fun insert(id: String, value: () -> GiftedCryptoToken): GiftedCryptoToken =
        withContext(Dispatchers.PlatformIO) {
            mutex.withLock {
                val database = databaseProvider.get()
                val token = value.invoke()

                require(id == token.id) {
                    "Cannot insert '${GiftedCryptoToken::class.simpleName}'. Provided id '$id' does not match model id '${token.id}'. "
                }

                database.giftedCryptoTokenQueries.insert(
                    id = token.id,
                    created = token.created,
                    updated = token.updated,
                    gifted = token.gifted,
                    address = token.address,
                    promoCode = token.promoCode,
                    message = token.message,
                    amount = token.amount.toMinorUnits().toDouble(),
                    amount_unit = Currency.Unit.Minor.serialName,
                    currency_type = token.amount.currency.type.value,
                    currency_code = token.amount.currency.code.value,
                    currency_default_fraction_digits = token.amount.currency.defaultFractionDigits?.toLong(),
                    currency_numeric_code = token.amount.currency.numericCode?.toLong(),
                    currency_symbol = token.amount.currency.symbol,
                    currency_name = token.amount.currency.name,
                    currency_ticker = token.amount.currency.ticker,
                    currency_chain_id = token.amount.currency.chainId,
                    currency_address = token.amount.currency.address
                )

                return@withContext token
            }
        }

    override suspend fun update(id: String, update: GiftedCryptoToken.() -> GiftedCryptoToken): GiftedCryptoToken =
        withContext(Dispatchers.PlatformIO) {
            mutex.withLock {
                val database = databaseProvider.get()

                database.transactionWithResult {
                    val current = database.giftedCryptoTokenQueries.selectById(id = id)
                        .executeAsOneOrNull()
                        ?.toGiftedCryptoToken()
                        ?: throw NoSuchElementException("No '${GiftedCryptoToken::class.simpleName}' found with id '$id'.")
                    val updated = current.update()

                    require(id == updated.id) {
                        "Cannot update '${GiftedCryptoToken::class.simpleName}'. Provided id '$id' does not match model id '${updated.id}'. "
                    }

                    database.giftedCryptoTokenQueries.updateAll(
                        id = updated.id,
                        updated = updated.updated,
                        gifted = updated.gifted,
                        address = updated.address,
                        promoCode = updated.promoCode,
                        message = updated.message,
                        amount = updated.amount.toMinorUnits().toDouble(),
                        amountUnit = Currency.Unit.Minor.serialName,
                        currencyType = updated.amount.currency.type.value,
                        currencyCode = updated.amount.currency.code.value,
                        currencyDefaultFractionDigits = updated.amount.currency.defaultFractionDigits?.toLong(),
                        currencyNumericCode = updated.amount.currency.numericCode?.toLong(),
                        currencySymbol = updated.amount.currency.symbol,
                        currencyName = updated.amount.currency.name,
                        currencyTicker = updated.amount.currency.ticker,
                        currencyChainId = updated.amount.currency.chainId,
                        currencyAddress = updated.amount.currency.address
                    )

                    updated
                }
            }
        }

    override suspend fun remove(id: String) {
        withContext(Dispatchers.PlatformIO) {
            mutex.withLock {
                val database = databaseProvider.get()

                database.giftedCryptoTokenQueries.deleteById(id = id)
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.PlatformIO) {
            mutex.withLock {
                val database = databaseProvider.get()

                database.giftedCryptoTokenQueries.deleteAll()
            }
        }
    }

    private fun Gifted_crypto_token.toGiftedCryptoToken(): GiftedCryptoToken =
        GiftedCryptoToken(
            id = this.id,
            created = this.created,
            updated = this.updated,
            gifted = this.gifted,
            promoCode = this.promoCode,
            message = this.message,
            address = this.address,
            amount = Currency.Amount.invoke(
                currency = Currency(
                    type = Currency.Type(value = this.currency_type),
                    code = Currency.Code(value = this.currency_code),
                    defaultFractionDigits = this.currency_default_fraction_digits?.toInt(),
                    numericCode = this.currency_numeric_code?.toInt(),
                    symbol = this.currency_symbol,
                    name = this.currency_name,
                    ticker = this.currency_ticker,
                    chainId = this.currency_chain_id,
                    address = this.currency_address
                ),
                unit = Currency.Unit[this.amount_unit] ?: Currency.Unit.Minor,
                value = this.amount
            )
        )
}
