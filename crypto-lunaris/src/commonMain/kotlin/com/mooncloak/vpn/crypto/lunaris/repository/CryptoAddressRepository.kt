package com.mooncloak.vpn.crypto.lunaris.repository

import com.mooncloak.vpn.crypto.lunaris.model.CryptoAddress
import com.mooncloak.vpn.data.shared.repository.MutableRepository
import kotlin.coroutines.cancellation.CancellationException

public interface CryptoAddressRepository : MutableRepository<CryptoAddress> {

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun getByAddress(address: String): CryptoAddress

    public suspend fun search(query: String): List<CryptoAddress>

    public companion object
}

public suspend fun CryptoAddressRepository.getByAddressOrNull(address: String): CryptoAddress? =
    try {
        getByAddress(address = address)
    } catch (_: NoSuchElementException) {
        null
    }
