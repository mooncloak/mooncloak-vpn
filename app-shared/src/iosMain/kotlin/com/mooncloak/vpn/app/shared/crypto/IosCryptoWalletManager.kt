package com.mooncloak.vpn.app.shared.crypto

import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager.Companion.LUNARIS_CONTRACT_ADDRESS
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager.Companion.POLYGON_MAINNET_RPC_URL
import com.mooncloak.vpn.crypto.lunaris.provider.CryptoWalletAddressProvider
import com.mooncloak.vpn.crypto.lunaris.repository.CryptoWalletRepository
import com.mooncloak.vpn.util.shared.crypto.AesEncryptor
import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.util.shared.currency.Lunaris
import kotlinx.datetime.Clock

public interface IosCryptoWalletManager : CryptoWalletManager {

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
