package com.mooncloak.vpn.app.shared.crypto

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.mooncloak.vpn.crypto.lunaris.BaseCryptoWalletManager
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager.Companion.LUNARIS_CONTRACT_ADDRESS
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager.Companion.POLYGON_MAINNET_RPC_URL
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet
import com.mooncloak.vpn.crypto.lunaris.model.EncryptedRecoveryPhrase
import com.mooncloak.vpn.crypto.lunaris.model.SendResult
import com.mooncloak.vpn.crypto.lunaris.provider.CryptoWalletAddressProvider
import com.mooncloak.vpn.crypto.lunaris.repository.CryptoWalletRepository
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import com.mooncloak.vpn.util.shared.crypto.AesEncryptor
import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.util.shared.currency.Lunaris
import com.mooncloak.vpn.util.shared.currency.Matic
import com.mooncloak.vpn.util.shared.currency.invoke
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

public abstract class IosCryptoWalletManager public constructor(
    private val cryptoWalletAddressProvider: CryptoWalletAddressProvider,
    private val polygonRpcUrl: String = POLYGON_MAINNET_RPC_URL,
    private val walletDirectoryPath: String,
    private val cryptoWalletRepository: CryptoWalletRepository,
    private val clock: Clock,
    private val currency: Currency = Currency.Lunaris,
    private val currencyAddress: String = LUNARIS_CONTRACT_ADDRESS,
    private val encryptor: AesEncryptor
) : BaseCryptoWalletManager(
    walletDirectoryPath = walletDirectoryPath,
    clock = clock,
    cryptoWalletAddressProvider = cryptoWalletAddressProvider,
    cryptoWalletRepository = cryptoWalletRepository,
    encryptor = encryptor
) {

    override suspend fun revealSeedPhrase(address: String, password: String?): String =
        withContext(Dispatchers.PlatformIO) {
            val wallet = cryptoWalletRepository.getByAddress(address = address)

            return@withContext decryptPhrase(
                phrase = wallet.phrase,
                password = password
            )
        }

    // Overriding to make it public so it is accessible to Swift code.
    public override suspend fun createAndStoreWallet(
        fileName: String,
        address: String,
        currency: Currency,
        phrase: EncryptedRecoveryPhrase
    ): CryptoWallet {
        return super.createAndStoreWallet(fileName, address, currency, phrase)
    }

    // Overriding to make it public so it is accessible to Swift code.
    public override suspend fun encryptPhrase(phrase: String, password: String?): EncryptedRecoveryPhrase {
        return super.encryptPhrase(phrase, password)
    }

    // Overriding to make it public so it is accessible to Swift code.
    public override suspend fun decryptPhrase(phrase: EncryptedRecoveryPhrase, password: String?): String {
        return super.decryptPhrase(phrase, password)
    }

    public suspend fun getWalletByAddressOrNull(address: String): CryptoWallet? =
        cryptoWalletRepository.getByAddress(address = address)

    public fun createLunarisAmountFromMinorUnit(value: Number): Currency.Amount =
        Currency.Amount(
            currency = Currency.Lunaris,
            unit = Currency.Unit.Minor,
            value = value
        )

    public fun createMaticAmountFromMinorUnit(value: Number): Currency.Amount =
        Currency.Amount(
            currency = Currency.Matic,
            unit = Currency.Unit.Minor,
            value = value
        )

    public fun createSuccessSendResult(
        txHash: String,
        gasUsed: Number? = null
    ): SendResult.Success = SendResult.Success(
        txHash = txHash,
        gasUsed = when (gasUsed) {
            null -> null
            is Byte -> BigInteger.fromByte(gasUsed)
            is Short -> BigInteger.fromShort(gasUsed)
            is Int -> BigInteger.fromInt(gasUsed)
            is Long -> BigInteger.fromLong(gasUsed)
            else -> BigInteger.fromLong(gasUsed.toLong())
        }
    )

    public fun createSuccessSendResult(
        txHash: String,
        gasUsed: UInt? = null
    ): SendResult.Success = SendResult.Success(
        txHash = txHash,
        gasUsed = gasUsed?.let { BigInteger.fromUInt(it) }
    )

    public fun createFailureSendResult(
        errorMessage: String
    ): SendResult.Failure = SendResult.Failure(
        errorMessage = errorMessage
    )

    public fun createPendingSendResult(
        txHash: String? = null
    ): SendResult.Pending = SendResult.Pending(
        txHash = txHash
    )

    public interface Factory {

        public fun create(
            cryptoWalletAddressProvider: CryptoWalletAddressProvider,
            polygonRpcUrl: String = POLYGON_MAINNET_RPC_URL,
            walletDirectoryPath: String,
            cryptoWalletRepository: CryptoWalletRepository,
            clock: Clock,
            currency: Currency = Currency.Lunaris,
            currencyAddress: String = LUNARIS_CONTRACT_ADDRESS,
            encryptor: AesEncryptor
        ): IosCryptoWalletManager

        public companion object
    }

    public companion object
}
