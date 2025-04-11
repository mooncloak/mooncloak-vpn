package com.mooncloak.vpn.crypto.lunaris.source

import com.mooncloak.vpn.crypto.lunaris.model.CryptoAddress
import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet
import com.mooncloak.vpn.crypto.lunaris.repository.CryptoAddressRepository
import com.mooncloak.vpn.data.sqlite.DatabaseManager
import com.mooncloak.vpn.data.sqlite.database.Crypto_address
import com.mooncloak.vpn.data.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

public operator fun CryptoAddressRepository.Companion.invoke(
    databaseProvider: DatabaseManager<MooncloakDatabase>
): CryptoAddressRepository = CryptoAddressDatabaseSource(
    databaseProvider = databaseProvider
)

internal class CryptoAddressDatabaseSource internal constructor(
    private val databaseProvider: DatabaseManager<MooncloakDatabase>
) : CryptoAddressRepository {

    private val mutex = Mutex(locked = false)

    override suspend fun get(id: String): CryptoAddress =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            database.cryptoAddressQueries.selectById(id = id)
                .executeAsOneOrNull()
                ?.toCryptoAddress()
                ?: throw NoSuchElementException("No '${CryptoWallet::class.simpleName}' found with id '$id'.")
        }

    override suspend fun getByAddress(address: String): CryptoAddress =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            database.cryptoAddressQueries.selectByAddress(address = address)
                .executeAsOneOrNull()
                ?.toCryptoAddress()
                ?: throw NoSuchElementException("No '${CryptoWallet::class.simpleName}' found with address '$address'.")
        }

    override suspend fun get(count: Int, offset: Int): List<CryptoAddress> =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            database.cryptoAddressQueries.selectPage(limit = count.toLong(), offset = offset.toLong())
                .executeAsList()
                .map { it.toCryptoAddress() }
        }

    override suspend fun getAll(): List<CryptoAddress> =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            database.cryptoAddressQueries.selectAll()
                .executeAsList()
                .map { it.toCryptoAddress() }
        }

    override suspend fun search(query: String): List<CryptoAddress> =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            database.cryptoAddressQueries.search(query = query)
                .executeAsList()
                .map { it.toCryptoAddress() }
        }

    override suspend fun insert(id: String, value: () -> CryptoAddress): CryptoAddress =
        withContext(Dispatchers.PlatformIO) {
            mutex.withLock {
                val database = databaseProvider.get()
                val address = value.invoke()

                require(id == address.id) { "id '$id' does not match value id '${address.id}'." }

                database.cryptoAddressQueries.insert(
                    id = address.id,
                    created = address.created,
                    updated = address.updated,
                    address = address.address,
                    name = address.displayName,
                    note = address.note,
                    currency_type = address.currency.type.value,
                    currency_code = address.currency.code.value,
                    currency_default_fraction_digits = address.currency.defaultFractionDigits?.toLong(),
                    currency_numeric_code = address.currency.numericCode?.toLong(),
                    currency_symbol = address.currency.symbol,
                    currency_name = address.currency.name,
                    currency_ticker = address.currency.ticker,
                    currency_chain_id = address.currency.chainId,
                    currency_address = address.currency.address,
                    handle = address.handle,
                    ens_name = address.ensName,
                    contact_id = address.contactId
                )

                return@withContext address
            }
        }

    override suspend fun update(id: String, update: CryptoAddress.() -> CryptoAddress): CryptoAddress =
        withContext(Dispatchers.PlatformIO) {
            mutex.withLock {
                val database = databaseProvider.get()

                database.transactionWithResult {
                    val current = database.cryptoAddressQueries.selectById(id = id)
                        .executeAsOneOrNull()
                        ?.toCryptoAddress()
                        ?: throw NoSuchElementException("No '${CryptoWallet::class.simpleName}' found with id '$id'.")
                    val updated = current.update()

                    require(id == updated.id) { "id '$id' does not match updated id '${updated.id}'." }

                    database.cryptoAddressQueries.updateAll(
                        id = updated.id,
                        updated = updated.updated,
                        address = updated.address,
                        name = updated.displayName,
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
                        handle = updated.handle,
                        ensName = updated.ensName,
                        contactId = updated.contactId
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

    private fun Crypto_address.toCryptoAddress(): CryptoAddress =
        CryptoAddress(
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
            ensName = this.ens_name,
            displayName = this.name,
            note = this.note,
            contactId = this.contact_id
        )
}
