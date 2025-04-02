package com.mooncloak.vpn.crypto.lunaris.repository

import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet
import com.mooncloak.vpn.data.shared.repository.MutableRepository
import kotlin.coroutines.cancellation.CancellationException

public interface CryptoWalletRepository : MutableRepository<CryptoWallet> {

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun getByAddress(address: String): CryptoWallet

    public companion object
}

public suspend fun CryptoWalletRepository.getByAddressOrNull(address: String): CryptoWallet? =
    try {
        getByAddress(address = address)
    } catch (_: NoSuchElementException) {
        null
    }
