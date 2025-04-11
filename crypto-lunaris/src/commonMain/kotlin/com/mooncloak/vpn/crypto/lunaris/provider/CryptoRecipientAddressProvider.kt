package com.mooncloak.vpn.crypto.lunaris.provider

import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.crypto.lunaris.model.CryptoAccount
import com.mooncloak.vpn.crypto.lunaris.model.CryptoAddress
import com.mooncloak.vpn.crypto.lunaris.repository.CryptoAddressRepository
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import com.mooncloak.vpn.util.shared.currency.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public interface CryptoRecipientAddressProvider {

    public suspend fun search(value: String): List<CryptoAddress>

    public companion object
}

public operator fun CryptoRecipientAddressProvider.Companion.invoke(
    cryptoWalletManager: CryptoWalletManager,
    cryptoAddressRepository: CryptoAddressRepository,
    currency: Currency,
    clock: Clock
): CryptoRecipientAddressProvider = DefaultCryptoRecipientAddressProvider(
    cryptoWalletManager = cryptoWalletManager,
    cryptoAddressRepository = cryptoAddressRepository,
    currency = currency,
    clock = clock
)

internal class DefaultCryptoRecipientAddressProvider internal constructor(
    private val cryptoWalletManager: CryptoWalletManager,
    private val cryptoAddressRepository: CryptoAddressRepository,
    private val currency: Currency,
    private val clock: Clock
) : CryptoRecipientAddressProvider {

    override suspend fun search(value: String): List<CryptoAddress> =
        withContext(Dispatchers.PlatformIO) {
            val deferredRecipient = async { cryptoWalletManager.resolveRecipient(value = value) }

            val addresses = cryptoAddressRepository.search(query = value)
            val recipientAddress = deferredRecipient.await()

            return@withContext if (recipientAddress != null) {
                addresses + recipientAddress.toCryptoAddress()
            } else {
                addresses
            }
        }

    @OptIn(ExperimentalUuidApi::class)
    private fun CryptoAccount.toCryptoAddress(): CryptoAddress {
        val now = clock.now()

        return CryptoAddress(
            id = Uuid.random().toHexString(),
            created = now,
            updated = now,
            address = this.address,
            ensName = this.name,
            currency = currency
        )
    }
}
