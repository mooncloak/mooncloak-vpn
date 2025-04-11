package com.mooncloak.vpn.crypto.lunaris

import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet
import com.mooncloak.vpn.crypto.lunaris.model.EncryptedRecoveryPhrase
import com.mooncloak.vpn.crypto.lunaris.model.decode
import com.mooncloak.vpn.crypto.lunaris.model.encodeToBase64UrlString
import com.mooncloak.vpn.crypto.lunaris.provider.CryptoWalletAddressProvider
import com.mooncloak.vpn.crypto.lunaris.repository.CryptoWalletRepository
import com.mooncloak.vpn.crypto.lunaris.repository.getByAddressOrNull
import com.mooncloak.vpn.util.shared.crypto.AesEncryptedData
import com.mooncloak.vpn.util.shared.crypto.AesEncryptor
import com.mooncloak.vpn.util.shared.crypto.decrypt
import com.mooncloak.vpn.util.shared.crypto.encrypt
import com.mooncloak.vpn.util.shared.currency.Currency
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public abstract class BaseCryptoWalletManager public constructor(
    private val walletDirectoryPath: String,
    private val clock: Clock,
    private val cryptoWalletAddressProvider: CryptoWalletAddressProvider,
    private val cryptoWalletRepository: CryptoWalletRepository,
    private val encryptor: AesEncryptor
) : CryptoWalletManager {

    override suspend fun getDefaultWallet(): CryptoWallet? =
        cryptoWalletAddressProvider.get()?.let { address ->
            cryptoWalletRepository.getByAddressOrNull(address)
        } ?: cryptoWalletRepository.getAll().firstOrNull()

    @OptIn(ExperimentalUuidApi::class)
    protected open suspend fun createAndStoreWallet(
        fileName: String,
        address: String,
        currency: Currency,
        phrase: EncryptedRecoveryPhrase
    ): CryptoWallet {
        val now = clock.now()
        val walletId = Uuid.random().toHexString()
        val wallet = CryptoWallet(
            id = walletId,
            created = now,
            updated = now,
            address = address,
            currency = currency,
            location = "$walletDirectoryPath/$fileName",
            phrase = phrase
        )

        return cryptoWalletRepository.insert(walletId) { wallet }
    }

    protected open suspend fun encryptPhrase(phrase: String, password: String?): EncryptedRecoveryPhrase {
        val encryptedData = if (password.isNullOrEmpty()) {
            AesEncryptedData(
                value = phrase.encodeToByteArray(),
                iv = ByteArray(0),
                salt = ByteArray(0)
            )
        } else {
            encryptor.encrypt(value = phrase, keyMaterial = password)
        }

        return EncryptedRecoveryPhrase(
            value = encryptedData.value.encodeToBase64UrlString(),
            iv = encryptedData.iv.encodeToBase64UrlString(),
            salt = encryptedData.salt.encodeToBase64UrlString(),
            algorithm = encryptedData.algorithm
        )
    }

    protected open suspend fun decryptPhrase(phrase: EncryptedRecoveryPhrase, password: String?): String {
        val aesData = AesEncryptedData(
            value = phrase.value.decode(),
            iv = phrase.iv.decode(),
            salt = phrase.salt.decode()
        )

        return if (aesData.iv.isEmpty() || password.isNullOrBlank()) {
            aesData.value.decodeToString()
        } else {
            encryptor.decrypt(data = aesData, keyMaterial = password).decodeToString()
        }
    }

    public companion object {

        public val ETHEREUM_ADDRESS_REGEX: Regex = Regex("^0x[a-fA-F0-9]{40}$")
    }
}
