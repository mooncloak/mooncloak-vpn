package com.mooncloak.vpn.crypto.lunaris

import com.mooncloak.vpn.crypto.lunaris.model.CryptoAccount
import com.mooncloak.vpn.crypto.lunaris.model.CryptoTransaction
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet
import com.mooncloak.vpn.crypto.lunaris.model.SendResult
import com.mooncloak.vpn.crypto.lunaris.model.TransactionStatus
import com.mooncloak.vpn.crypto.lunaris.model.TransactionType
import com.mooncloak.vpn.util.shared.currency.Currency

public operator fun CryptoWalletManager.Companion.invoke(): CryptoWalletManager = IosCryptoWalletManager()

internal class IosCryptoWalletManager internal constructor() : CryptoWalletManager {

    override suspend fun getBalance(address: String): Currency.Amount {
        TODO("Not yet implemented")
    }

    override suspend fun createWallet(password: String?): CryptoWallet {
        TODO("Not yet implemented")
    }

    override suspend fun restoreWallet(phrase: String, password: String?): CryptoWallet {
        TODO("Not yet implemented")
    }

    override suspend fun getDefaultWallet(): CryptoWallet? {
        TODO("Not yet implemented")
    }

    override suspend fun revealSeedPhrase(address: String, password: String?): String {
        TODO("Not yet implemented")
    }

    override suspend fun getTransactionHistory(
        address: String,
        offset: Int,
        count: Int,
        type: TransactionType
    ): List<CryptoTransaction> {
        TODO("Not yet implemented")
    }

    override suspend fun getTransactionStatus(txHash: String): TransactionStatus? {
        TODO("Not yet implemented")
    }

    override suspend fun send(origin: String, password: String?, target: String, amount: Currency.Amount): SendResult {
        TODO("Not yet implemented")
    }

    override suspend fun estimateGas(origin: String, target: String, amount: Currency.Amount): Currency.Amount? {
        TODO("Not yet implemented")
    }

    override suspend fun resolveRecipient(value: String): CryptoAccount? {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }
}
