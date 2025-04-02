package com.mooncloak.vpn.crypto.lunaris

import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet
import com.mooncloak.vpn.crypto.lunaris.provider.CryptoWalletAddressProvider
import com.mooncloak.vpn.crypto.lunaris.repository.CryptoWalletRepository
import com.mooncloak.vpn.crypto.lunaris.repository.getByAddressOrNull

internal abstract class BaseCryptoWalletApi internal constructor(
    private val cryptoWalletAddressProvider: CryptoWalletAddressProvider,
    private val cryptoWalletRepository: CryptoWalletRepository
) : CryptoWalletApi {

    override suspend fun getDefaultWallet(): CryptoWallet? =
        cryptoWalletAddressProvider.get()?.let { address ->
            cryptoWalletRepository.getByAddressOrNull(address)
        } ?: cryptoWalletRepository.getAll().firstOrNull()
}
