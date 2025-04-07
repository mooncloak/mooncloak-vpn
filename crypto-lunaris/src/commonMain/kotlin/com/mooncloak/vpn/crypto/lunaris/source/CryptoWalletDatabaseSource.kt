package com.mooncloak.vpn.crypto.lunaris.source

import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet
import com.mooncloak.vpn.crypto.lunaris.model.EncryptedRecoveryPhrase
import com.mooncloak.vpn.crypto.lunaris.model.decode
import com.mooncloak.vpn.crypto.lunaris.model.encodeToBase64UrlString
import com.mooncloak.vpn.crypto.lunaris.repository.CryptoWalletRepository
import com.mooncloak.vpn.data.sqlite.DatabaseManager
import com.mooncloak.vpn.data.sqlite.database.Crypto_wallet
import com.mooncloak.vpn.data.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

public operator fun CryptoWalletRepository.Companion.invoke(
    databaseProvider: DatabaseManager<MooncloakDatabase>
): CryptoWalletRepository = CryptoWalletDatabaseSource(
    databaseProvider = databaseProvider
)

internal class CryptoWalletDatabaseSource internal constructor(
    private val databaseProvider: DatabaseManager<MooncloakDatabase>
) : CryptoWalletRepository {

    private val mutex = Mutex(locked = false)

    override suspend fun get(id: String): CryptoWallet =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            database.cryptoWalletQueries.selectById(id = id)
                .executeAsOneOrNull()
                ?.toCryptoWallet()
                ?: throw NoSuchElementException("No '${CryptoWallet::class.simpleName}' found with id '$id'.")
        }

    override suspend fun getByAddress(address: String): CryptoWallet =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            database.cryptoWalletQueries.selectByAddress(address = address)
                .executeAsOneOrNull()
                ?.toCryptoWallet()
                ?: throw NoSuchElementException("No '${CryptoWallet::class.simpleName}' found with address '$address'.")
        }

    override suspend fun get(count: Int, offset: Int): List<CryptoWallet> =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            database.cryptoWalletQueries.selectPage(limit = count.toLong(), offset = offset.toLong())
                .executeAsList()
                .map { it.toCryptoWallet() }
        }

    override suspend fun getAll(): List<CryptoWallet> =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            database.cryptoWalletQueries.selectAll()
                .executeAsList()
                .map { it.toCryptoWallet() }
        }

    override suspend fun insert(id: String, value: () -> CryptoWallet): CryptoWallet =
        withContext(Dispatchers.PlatformIO) {
            mutex.withLock {
                val database = databaseProvider.get()
                val wallet = value.invoke()

                require(id == wallet.id) { "id '$id' does not match value id '${wallet.id}'." }

                database.cryptoWalletQueries.insert(
                    id = wallet.id,
                    created = wallet.created,
                    updated = wallet.updated,
                    address = wallet.address,
                    location = wallet.location,
                    name = wallet.name,
                    note = wallet.note,
                    currency_type = wallet.currency.type.value,
                    currency_code = wallet.currency.code.value,
                    currency_default_fraction_digits = wallet.currency.defaultFractionDigits?.toLong(),
                    currency_numeric_code = wallet.currency.numericCode?.toLong(),
                    currency_symbol = wallet.currency.symbol,
                    currency_name = wallet.currency.name,
                    currency_ticker = wallet.currency.ticker,
                    currency_chain_id = wallet.currency.chainId,
                    currency_address = wallet.currency.address,
                    enc_phrase = wallet.phrase.value.decode(),
                    enc_iv = wallet.phrase.iv.decode(),
                    enc_salt = wallet.phrase.salt.decode(),
                    enc_alg = wallet.phrase.algorithm
                )

                return@withContext wallet
            }
        }

    override suspend fun update(id: String, update: CryptoWallet.() -> CryptoWallet): CryptoWallet =
        withContext(Dispatchers.PlatformIO) {
            mutex.withLock {
                val database = databaseProvider.get()

                database.transactionWithResult {
                    val current = database.cryptoWalletQueries.selectById(id = id)
                        .executeAsOneOrNull()
                        ?.toCryptoWallet()
                        ?: throw NoSuchElementException("No '${CryptoWallet::class.simpleName}' found with id '$id'.")
                    val updated = current.update()

                    require(id == updated.id) { "id '$id' does not match updated id '${updated.id}'." }

                    database.cryptoWalletQueries.updateAll(
                        id = updated.id,
                        updated = updated.updated,
                        address = updated.address,
                        location = updated.location,
                        name = updated.name,
                        note = updated.note,
                        currencyType = updated.currency.type.value,
                        currencyCode = updated.currency.code.value,
                        currencyDefaultFractionDigits = updated.currency.defaultFractionDigits?.toLong(),
                        currencyNumericCode = updated.currency.numericCode?.toLong(),
                        currencySymbol = updated.currency.symbol,
                        currencyName = updated.currency.name,
                        currencyTicker = updated.currency.ticker,
                        currencyChainId = updated.currency.chainId,
                        currencyAddress = updated.currency.address,
                        encPhrase = updated.phrase.value.decode(),
                        encIv = updated.phrase.iv.decode(),
                        encSalt = updated.phrase.salt.decode(),
                        encAlg = updated.phrase.algorithm
                    )

                    updated
                }
            }
        }

    override suspend fun remove(id: String) {
        withContext(Dispatchers.PlatformIO) {
            mutex.withLock {
                val database = databaseProvider.get()

                database.cryptoWalletQueries.deleteById(id = id)
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.PlatformIO) {
            mutex.withLock {
                val database = databaseProvider.get()

                database.cryptoWalletQueries.deleteAll()
            }
        }
    }

    private fun Crypto_wallet.toCryptoWallet(): CryptoWallet =
        CryptoWallet(
            id = this.id,
            created = this.created,
            updated = this.updated,
            address = this.address,
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
            location = this.location,
            name = this.name,
            note = this.note,
            phrase = EncryptedRecoveryPhrase(
                value = this.enc_phrase.encodeToBase64UrlString(),
                iv = this.enc_iv.encodeToBase64UrlString(),
                salt = this.enc_salt.encodeToBase64UrlString(),
                algorithm = this.enc_alg
            )
        )
}
