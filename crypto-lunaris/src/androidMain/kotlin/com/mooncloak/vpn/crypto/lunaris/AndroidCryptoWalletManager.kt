package com.mooncloak.vpn.crypto.lunaris

import android.content.Context
import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.util.shared.currency.Lunaris
import com.mooncloak.vpn.util.shared.currency.invoke
import com.mooncloak.vpn.crypto.lunaris.model.CryptoAccount
import com.mooncloak.vpn.crypto.lunaris.model.CryptoTransaction
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet
import com.mooncloak.vpn.crypto.lunaris.model.EncryptedRecoveryPhrase
import com.mooncloak.vpn.crypto.lunaris.model.SendResult
import com.mooncloak.vpn.crypto.lunaris.model.TransactionStatus
import com.mooncloak.vpn.crypto.lunaris.model.TransactionType
import com.mooncloak.vpn.crypto.lunaris.model.decode
import com.mooncloak.vpn.crypto.lunaris.model.encodeToBase64UrlString
import com.mooncloak.vpn.crypto.lunaris.provider.CryptoWalletAddressProvider
import com.mooncloak.vpn.crypto.lunaris.repository.CryptoWalletRepository
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import com.mooncloak.vpn.util.shared.crypto.AesEncryptedData
import com.mooncloak.vpn.util.shared.crypto.AesEncryptor
import com.mooncloak.vpn.util.shared.crypto.decrypt
import com.mooncloak.vpn.util.shared.crypto.encrypt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.MnemonicUtils
import org.web3j.crypto.RawTransaction
import org.web3j.crypto.TransactionEncoder
import org.web3j.crypto.WalletUtils
import org.web3j.ens.EnsResolver
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Numeric
import java.io.File
import java.math.BigInteger
import java.security.SecureRandom
import kotlin.jvm.optionals.getOrNull
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public operator fun CryptoWalletManager.Companion.invoke(
    cryptoWalletAddressProvider: CryptoWalletAddressProvider,
    polygonRpcUrl: String = POLYGON_MAINNET_RPC_URL,
    walletDirectoryPath: String,
    cryptoWalletRepository: CryptoWalletRepository,
    clock: Clock,
    currency: Currency = Currency.Lunaris,
    currencyAddress: String = LUNARIS_CONTRACT_ADDRESS,
    encryptor: AesEncryptor
): CryptoWalletManager = AndroidCryptoWalletManager(
    cryptoWalletAddressProvider = cryptoWalletAddressProvider,
    polygonRpcUrl = polygonRpcUrl,
    walletDirectoryPath = walletDirectoryPath,
    cryptoWalletRepository = cryptoWalletRepository,
    clock = clock,
    currency = currency,
    currencyAddress = currencyAddress,
    encryptor = encryptor
)

internal class AndroidCryptoWalletManager internal constructor(
    cryptoWalletAddressProvider: CryptoWalletAddressProvider,
    polygonRpcUrl: String = CryptoWalletManager.POLYGON_MAINNET_RPC_URL,
    private val walletDirectoryPath: String,
    private val cryptoWalletRepository: CryptoWalletRepository,
    private val clock: Clock,
    private val currency: Currency = Currency.Lunaris,
    private val currencyAddress: String,
    private val encryptor: AesEncryptor
) : BaseCryptoWalletManager(
    cryptoWalletAddressProvider = cryptoWalletAddressProvider,
    cryptoWalletRepository = cryptoWalletRepository
) {

    private val web3j: Web3j = Web3j.build(HttpService(polygonRpcUrl))
    private val ensResolver = EnsResolver(web3j)

    override suspend fun getBalance(address: String): Currency.Amount =
        withContext(Dispatchers.PlatformIO) {
            // For ERC-20 token balance
            val function = Function(
                "balanceOf",
                listOf(Address(address)),
                listOf(TypeReference.create(Uint256::class.java))
            )
            val encodedFunction = FunctionEncoder.encode(function)
            val ethCall = web3j.ethCall(
                org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(
                    address,
                    currencyAddress,
                    encodedFunction
                ),
                DefaultBlockParameterName.LATEST
            ).send()

            val balanceWei = BigInteger(ethCall.value.substring(2), 16)

            return@withContext Currency.Amount(
                currency = currency,
                unit = Currency.Unit.Minor,
                value = balanceWei
            )
        }

    override suspend fun createWallet(password: String?): CryptoWallet =
        withContext(Dispatchers.PlatformIO) {
            val walletFileDir = File(walletDirectoryPath)

            // Generate random entropy and mnemonic (no temp file)
            val entropy = ByteArray(16).apply { SecureRandom().nextBytes(this) }  // 16 bytes = 12 words
            val mnemonic = MnemonicUtils.generateMnemonic(entropy)

            val credentials = WalletUtils.loadBip39Credentials("", mnemonic)

            // Encrypt the wallet file with the provided password (or empty if null)
            val encryptionPassword = password ?: ""
            val encryptedWalletFile = WalletUtils.generateWalletFile(
                encryptionPassword,
                credentials.ecKeyPair,
                walletFileDir,
                false
            )

            val encryptedPhrase = encryptPhrase(phrase = mnemonic, password = password)

            return@withContext createAndStoreWallet(
                fileName = encryptedWalletFile,
                address = credentials.address,
                currency = currency,
                phrase = encryptedPhrase
            )
        }

    override suspend fun restoreWallet(phrase: String, password: String?): CryptoWallet =
        withContext(Dispatchers.PlatformIO) {
            // Validate seed phrase (basic check)
            val words = phrase.trim().split("\\s+".toRegex())

            if (words.size != 12 && words.size != 24) {
                throw IllegalArgumentException("Invalid seed phrase: must be 12 or 24 words")
            }

            // Do NOT use the password here. Use an empty String. Otherwise, the password will be used as the "25th"
            // word in the recovery phrase and if lost the wallet can't be recovered.
            val credentials = WalletUtils.loadBip39Credentials("", phrase)
            val walletFileName = WalletUtils.generateWalletFile(
                password ?: "",
                credentials.ecKeyPair,
                File(walletDirectoryPath),
                false
            )

            val encryptedPhrase = encryptPhrase(phrase = phrase, password = password)

            return@withContext createAndStoreWallet(
                fileName = walletFileName,
                address = credentials.address,
                currency = currency,
                phrase = encryptedPhrase
            )
        }

    override suspend fun revealSeedPhrase(address: String, password: String?): String =
        withContext(Dispatchers.PlatformIO) {
            val wallet = cryptoWalletRepository.getByAddress(address = address)

            return@withContext decryptPhrase(
                phrase = wallet.phrase,
                password = password
            )
        }

    override suspend fun getTransactionHistory(
        address: String,
        offset: Int,
        count: Int,
        type: TransactionType
    ): List<CryptoTransaction> {
        TODO("Not yet implemented")
    }

    override suspend fun getTransactionStatus(txHash: String): TransactionStatus =
        withContext(Dispatchers.PlatformIO) {
            val receipt: TransactionReceipt? =
                web3j.ethGetTransactionReceipt(txHash).send().transactionReceipt.getOrNull()

            return@withContext when {
                receipt == null -> TransactionStatus.PENDING
                receipt.isStatusOK -> TransactionStatus.CONFIRMED
                else -> TransactionStatus.FAILED
            }
        }

    override suspend fun send(
        origin: String,
        password: String?,
        target: String,
        amount: Currency.Amount
    ): SendResult =
        withContext(Dispatchers.PlatformIO) {
            try {
                val wallet = cryptoWalletRepository.getByAddress(origin)
                val credentials = WalletUtils.loadCredentials(password ?: "", File(wallet.location))

                // For ERC-20 token transfer
                val function = Function(
                    "transfer",
                    listOf(Address(target), Uint256(amount.toMinorUnits())),
                    emptyList()
                )
                val encodedFunction = FunctionEncoder.encode(function)

                val nonce = web3j.ethGetTransactionCount(
                    credentials.address,
                    DefaultBlockParameterName.LATEST
                ).send().transactionCount

                // Get current gas price (more dynamic than default)
                val gasPrice = web3j.ethGasPrice().send().gasPrice
                val gasLimit = try {
                    web3j.ethEstimateGas(
                        org.web3j.protocol.core.methods.request.Transaction.createFunctionCallTransaction(
                            credentials.address,
                            nonce,
                            gasPrice,
                            null,
                            currencyAddress,
                            BigInteger.ZERO,
                            encodedFunction
                        )
                    ).send().amountUsed
                } catch (e: Exception) {
                    BigInteger.valueOf(65000) // Adjust for Polygon
                }

                val chainId = currency.chainId ?: 137
                val rawTransaction = RawTransaction.createTransaction(
                    nonce,
                    gasPrice,
                    gasLimit,
                    currencyAddress,
                    BigInteger.ZERO,
                    encodedFunction
                )

                val signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials)
                val hexValue = Numeric.toHexString(signedMessage)

                val transaction = web3j.ethSendRawTransaction(hexValue).send()

                if (transaction.hasError()) {
                    return@withContext SendResult.Failure(
                        errorMessage = "Send transaction failed: ${transaction.error.code}: ${transaction.error.message}"
                    )
                }

                val receipt = web3j.ethGetTransactionReceipt(transaction.transactionHash)
                    .send()
                    .transactionReceipt
                    .getOrNull()

                return@withContext if (receipt != null && receipt.isStatusOK) {
                    SendResult.Success(
                        txHash = transaction.transactionHash,
                        gasUsed = receipt.gasUsed?.let {
                            com.ionspin.kotlin.bignum.integer.BigInteger.parseString(it.toString())
                        }
                    )
                } else {
                    SendResult.Pending(txHash = transaction.transactionHash)
                }
            } catch (e: Exception) {
                return@withContext SendResult.Failure(
                    errorMessage = "Send transaction failed: ${e.message}"
                )
            }
        }

    override suspend fun estimateGas(
        origin: String,
        target: String,
        amount: Currency.Amount
    ): Currency.Amount? =
        withContext(Dispatchers.PlatformIO) {
            try {
                // Get nonce
                val nonce = web3j.ethGetTransactionCount(origin, DefaultBlockParameterName.LATEST)
                    .send()
                    .transactionCount

                // Create transaction object
                val transaction = org.web3j.protocol.core.methods.request.Transaction(
                    origin,
                    nonce,
                    null, // Gas price (null for estimation)
                    null, // Gas limit (null for estimation)
                    target,
                    amount.toMinorUnits().toBigInteger(),
                    "" // data empty for native transfer
                )

                // Estimate gas
                val gasEstimate = web3j.ethEstimateGas(transaction)
                    .send()
                    .amountUsed

                return@withContext Currency.Amount(
                    currency = amount.currency,
                    unit = Currency.Unit.Minor,
                    value = gasEstimate
                )
            } catch (e: Exception) {
                return@withContext null
            }
        }

    override suspend fun resolveRecipient(value: String): CryptoAccount? =
        withContext(Dispatchers.PlatformIO) {
            // Check if input is a valid address
            if (value.matches(ETHEREUM_ADDRESS_REGEX)) {
                val name = runCatching { ensResolver.reverseResolve(value) }.getOrNull()

                return@withContext CryptoAccount(
                    address = value,
                    name = name
                )
            }

            // Check if input is a full ENS name (e.g., "Chris.eth")
            if (value.endsWith(".eth")) {
                try {
                    val address = ensResolver.resolve(value)

                    if (address != null) {
                        return@withContext CryptoAccount(
                            address = address,
                            name = value
                        )
                    }
                } catch (e: Exception) {
                    // ENS resolution failed, move to next check
                }
            }

            // If not an address or full ENS, test input + ".eth" (e.g., "Chris" -> "Chris.eth")
            val ensCandidate = "$value.eth"
            try {
                val address = ensResolver.resolve(ensCandidate)

                if (address != null) {
                    return@withContext CryptoAccount(
                        address = address,
                        name = ensCandidate
                    )
                }
            } catch (e: Exception) {
                // No valid resolution
            }

            return@withContext null
        }

    override fun close() {
        web3j.shutdown()
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun createAndStoreWallet(
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

    private suspend fun encryptPhrase(phrase: String, password: String?): EncryptedRecoveryPhrase {
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

    private suspend fun decryptPhrase(phrase: EncryptedRecoveryPhrase, password: String?): String {
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

    internal companion object {

        internal val ETHEREUM_ADDRESS_REGEX = Regex("^0x[a-fA-F0-9]{40}$")
    }
}

public fun CryptoWalletManager.Companion.walletDirectory(context: Context): File =
    File(context.filesDir, DEFAULT_WALLET_DIRECTORY_NAME).apply {
        mkdirs()
    }
