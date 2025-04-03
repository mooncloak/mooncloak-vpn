package com.mooncloak.vpn.crypto.lunaris

import com.mooncloak.vpn.api.shared.currency.Currency
import com.mooncloak.vpn.api.shared.currency.Lunaris
import com.mooncloak.vpn.api.shared.currency.invoke
import com.mooncloak.vpn.crypto.lunaris.model.CryptoAccount
import com.mooncloak.vpn.crypto.lunaris.model.CryptoTransaction
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet
import com.mooncloak.vpn.crypto.lunaris.model.SendResult
import com.mooncloak.vpn.crypto.lunaris.model.TransactionStatus
import com.mooncloak.vpn.crypto.lunaris.model.TransactionType
import com.mooncloak.vpn.crypto.lunaris.provider.CryptoWalletAddressProvider
import com.mooncloak.vpn.crypto.lunaris.repository.CryptoWalletRepository
import kotlinx.datetime.Clock
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.WalletUtils
import org.web3j.ens.EnsResolver
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.TransactionReceipt
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import java.io.File
import java.math.BigInteger
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
    currencyAddress: String = LUNARIS_CONTRACT_ADDRESS
): CryptoWalletManager = AndroidCryptoWalletManager(
    cryptoWalletAddressProvider = cryptoWalletAddressProvider,
    polygonRpcUrl = polygonRpcUrl,
    walletDirectoryPath = walletDirectoryPath,
    cryptoWalletRepository = cryptoWalletRepository,
    clock = clock,
    currency = currency,
    currencyAddress = currencyAddress
)

internal class AndroidCryptoWalletManager internal constructor(
    cryptoWalletAddressProvider: CryptoWalletAddressProvider,
    polygonRpcUrl: String = CryptoWalletManager.POLYGON_MAINNET_RPC_URL,
    private val walletDirectoryPath: String,
    private val cryptoWalletRepository: CryptoWalletRepository,
    private val clock: Clock,
    private val currency: Currency = Currency.Lunaris,
    private val currencyAddress: String // Replace with actual LNRS contract address
) : BaseCryptoWalletManager(
    cryptoWalletAddressProvider = cryptoWalletAddressProvider,
    cryptoWalletRepository = cryptoWalletRepository
) {

    private val web3j: Web3j = Web3j.build(HttpService(polygonRpcUrl))
    private val ensResolver = EnsResolver(web3j)

    override suspend fun getBalance(address: String): Currency.Amount {
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

        return Currency.Amount(
            currency = currency,
            unit = Currency.Unit.Minor,
            value = balanceWei
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createWallet(password: String?): CryptoWallet {
        val walletFileName = WalletUtils.generateNewWalletFile(
            password,
            File(walletDirectoryPath),
            true
        )
        val credentials = WalletUtils.loadCredentials(
            password,
            File(walletDirectoryPath, walletFileName)
        )
        val now = clock.now()

        val walletId = Uuid.random().toHexString()
        val wallet = CryptoWallet(
            id = walletId,
            created = now,
            updated = now,
            address = credentials.address,
            currency = currency,
            location = "$walletDirectoryPath/$walletFileName"
        )

        return cryptoWalletRepository.insert(walletId) { wallet }
    }

    override suspend fun revealSeedPhrase(address: String, password: String?): String {
        val wallet = cryptoWalletRepository.getByAddress(address = address)
        val credentials = WalletUtils.loadCredentials(
            password,
            File(wallet.location)
        )

        return credentials.ecKeyPair.privateKey.toString(16)
    }

    override suspend fun getTransactionHistory(
        address: String,
        offset: Int,
        count: Int,
        type: TransactionType
    ): List<CryptoTransaction> {
        TODO("Not yet implemented")
    }

    override suspend fun getTransactionStatus(txHash: String): TransactionStatus {
        val receipt: TransactionReceipt? = web3j.ethGetTransactionReceipt(txHash).send().transactionReceipt.getOrNull()

        return when {
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
    ): SendResult {
        val wallet = cryptoWalletRepository.getByAddress(origin)
        val credentials = WalletUtils.loadCredentials(password, File(wallet.location))

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

        val transactionHash = web3j.ethSendTransaction(
            org.web3j.protocol.core.methods.request.Transaction.createFunctionCallTransaction(
                credentials.address,           // from
                nonce,                         // nonce
                DefaultGasProvider.GAS_PRICE,  // gasPrice
                DefaultGasProvider.GAS_LIMIT,  // gasLimit
                currencyAddress,               // to (contract address)
                BigInteger.ZERO,               // value (0 for ERC-20 transfer)
                encodedFunction                // data
            )
        ).send().transactionHash

        // TODO: Get gas used + properly handle status.

        return SendResult.Success(transactionHash)
    }

    override suspend fun resolveRecipient(value: String): CryptoAccount? {
        // Check if input is a valid address
        if (value.matches(ETHEREUM_ADDRESS_REGEX)) {
            val name = runCatching { ensResolver.reverseResolve(value) }.getOrNull()

            return CryptoAccount(
                address = value,
                name = name
            )
        }

        // Check if input is a full ENS name (e.g., "Chris.eth")
        if (value.endsWith(".eth")) {
            try {
                val address = ensResolver.resolve(value)

                if (address != null) {
                    return CryptoAccount(
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
                return CryptoAccount(
                    address = address,
                    name = ensCandidate
                )
            }
        } catch (e: Exception) {
            // No valid resolution
        }

        return null
    }

    override fun close() {
        web3j.shutdown()
    }

    internal companion object {

        internal val ETHEREUM_ADDRESS_REGEX = Regex("^0x[a-fA-F0-9]{40}$")
    }
}
