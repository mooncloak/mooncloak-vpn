package com.mooncloak.vpn.crypto.lunaris

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.crypto.lunaris.model.CryptoAccount
import com.mooncloak.vpn.crypto.lunaris.model.CryptoTransaction
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet
import com.mooncloak.vpn.crypto.lunaris.model.SendResult
import com.mooncloak.vpn.crypto.lunaris.model.TransactionStatus
import com.mooncloak.vpn.crypto.lunaris.model.TransactionType

/**
 * Interface defining core operations for the Lunaris (LNRS) cryptocurrency wallet on the Polygon network.
 * This API provides functionality for managing a wallet, sending and receiving LNRS, and querying transaction details.
 * It extends [AutoCloseable] to ensure resources are properly released when the API is no longer needed.
 */
public interface CryptoWalletManager : AutoCloseable {

    /**
     * Retrieves the current balance of the wallet in LNRS.
     *
     * @param [address] The public address of the wallet.
     *
     * @return The balance in LNRS as a [BigDecimal], representing the amount of LNRS available in the wallet.
     *
     * @throws IllegalStateException if no wallet has been created or loaded.
     */
    public suspend fun getBalance(address: String): Currency.Amount

    /**
     * Creates a new wallet and initializes it for use.
     *
     * @return A [CryptoWallet] object containing details about the newly created wallet.
     */
    public suspend fun createWallet(password: String? = null): CryptoWallet

    /**
     * Restores a wallet from a BIP-39 seed phrase (mnemonic).
     *
     * @param [phrase] The BIP-39 mnemonic seed phrase (e.g., 12 or 24 words).
     *
     * @param [password] An optional password for additional encryption (null if using autogenerated).
     *
     * @return A [CryptoWallet] object containing details about the restored wallet.
     *
     * @throws IllegalArgumentException if the seed phrase is invalid.
     */
    public suspend fun restoreWallet(phrase: String, password: String? = null): CryptoWallet

    /**
     * Retrieves the current wallet, if one exists.
     *
     * @return The current [CryptoWallet] object, or null if no wallet is initialized.
     *
     * @throws IllegalStateException if no wallet has been created or loaded.
     */
    public suspend fun getDefaultWallet(): CryptoWallet?

    /**
     * Reveals the seed phrase (mnemonic) for the current wallet, used for backup or recovery.
     *
     * @param [address] The public address of the wallet.
     *
     * @param [password] An optional password for decrypting the wallet file (null if unencrypted).
     *
     * @return The BIP-39 mnemonic seed phrase as a string (e.g., 12 or 24 words).
     *
     * @throws IllegalStateException if no wallet has been created or loaded.
     *
     * @throws RuntimeException if the password is incorrect or the wallet file cannot be decrypted.
     */
    public suspend fun revealSeedPhrase(address: String, password: String? = null): String

    /**
     * Fetches a paginated list of transactions associated with the wallet.
     *
     * @param [address] The public address of the wallet.
     *
     * @param [offset] The page number to fetch (1-based indexing, default is 1).
     *
     * @param [count] The number of transactions per page (default is 10).
     *
     * @param [type] The type of transactions to filter by (default is [TransactionType.ALL]).
     *
     * @return A list of [CryptoTransaction] objects representing the wallet's transaction history for the specified page.
     *
     * @throws IllegalStateException if no wallet has been created or loaded.
     *
     * @throws IllegalArgumentException if [offset] is less than 1 or [count] is not positive.
     */
    public suspend fun getTransactionHistory(
        address: String,
        offset: Int = 1,
        count: Int = 10,
        type: TransactionType = TransactionType.ALL
    ): List<CryptoTransaction>

    /**
     * Checks the status of a specific transaction by its hash.
     *
     * @param [txHash] The transaction hash as a hexadecimal string (e.g., "0x...").
     *
     * @return The [TransactionStatus] of the transaction (e.g., PENDING, CONFIRMED, FAILED).
     */
    public suspend fun getTransactionStatus(txHash: String): TransactionStatus?

    /**
     * Sends LNRS to a specified recipient address on the Polygon network.
     *
     * @param [origin] The public address of the wallet to send funds from.
     *
     * @param [password] An optional password for decrypting the wallet file (null if unencrypted).
     *
     * @param [target] The recipient's wallet address as a hexadecimal string (e.g., "0x...").
     *
     * @param [amount] The amount of LNRS to send, specified as a [BigDecimal].
     *
     * @return A [SendResult] indicating the outcome of the transaction (success, failure, or pending).
     *
     * @throws IllegalStateException if no wallet has been created or loaded.
     *
     * @throws RuntimeException if the transaction fails due to network issues or insufficient funds.
     */
    public suspend fun send(
        origin: String,
        password: String? = null,
        target: String,
        amount: Currency.Amount
    ): SendResult

    /**
     * Estimates the cost of gas, or the price to perform the transaction.
     *
     * @param [origin] The public address of the wallet to send funds from.
     *
     * @param [target] The recipient's wallet address as a hexadecimal string (e.g., "0x...").
     *
     * @param [amount] The amount of LNRS to send, specified as a [BigDecimal].
     *
     * @return The estimated [Currency.Amount] of gas required to perform the transaction at this moment in time, or
     * `null` if unknown.
     */
    public suspend fun estimateGas(
        origin: String,
        target: String,
        amount: Currency.Amount
    ): Currency.Amount?

    /**
     * Attempts to resolve the provided [value] as a [CryptoAccount].
     *
     * @param [value] The address, ENS name, or query to search for.
     *
     * @return The [CryptoAccount] or `null` if none matching was found.
     */
    public suspend fun resolveRecipient(value: String): CryptoAccount?

    /**
     * Closes the API, releasing any underlying resources such as network connections.
     * Implementations should override this to ensure proper cleanup.
     */
    override fun close()

    public companion object {

        public const val POLYGON_MAINNET_RPC_URL: String = "https://polygon-rpc.com"
        public const val LUNARIS_CONTRACT_ADDRESS: String = "0x23C2d7673Dd36FF6cBD642A5a70f58c1D2118C13"
        public const val LUNARIS_UNISWAP_POOL_URI: String = "https://app.uniswap.org/explore/tokens/polygon/0x23c2d7673dd36ff6cbd642a5a70f58c1d2118c13"
        public const val DEFAULT_WALLET_DIRECTORY_NAME: String = "wallets"
    }
}

/**
 * Retrieves the current wallet, if one exists, otherwise creates a new wallet.
 *
 * @return The current [CryptoWallet] object, or null if no wallet is initialized.
 *
 * @throws IllegalStateException if no wallet has been created or loaded.
 */
public suspend fun CryptoWalletManager.getDefaultOrCreateWallet(password: String? = null): CryptoWallet =
    getDefaultWallet() ?: createWallet(password = password)
