//
//  SwiftCryptoWalletManager.swift
//  app-ios
//
//  Created by Chris Keenan on 4/9/25.
//

import Foundation
import web3swift
import Web3Core
import app_shared
import BigInt

@objc public class SwiftCryptoWalletManager: NSObject, IosCryptoWalletManager {
    
    private var web3: Web3?
    private var wallets: [String: BIP32Keystore] = [:] // In-memory wallet store
    private let walletDirectoryPath: String
    private let cryptoWalletRepository: Crypto_lunarisCryptoWalletRepository
    private let currencyAddress: String
    private let encryptor: Util_sharedAesEncryptor
    
    // Assuming these are passed in via a factory method similar to Android
    init(
        walletDirectoryPath: String,
        cryptoWalletRepository: Crypto_lunarisCryptoWalletRepository,
        currencyAddress: String,
        encryptor: Util_sharedAesEncryptor
    ) {
        self.walletDirectoryPath = walletDirectoryPath
        self.cryptoWalletRepository = cryptoWalletRepository
        self.currencyAddress = currencyAddress
        self.encryptor = encryptor
        super.init()
    }

    public func createWallet(password: String?) async throws -> Crypto_lunarisCryptoWallet {
        <#code#>
    }
    
    public func estimateGas(origin: String, target: String, amount: Util_sharedCurrencyAmount) async throws -> Util_sharedCurrencyAmount? {
        <#code#>
    }
    
    public func getBalance(address: String) async throws -> Util_sharedCurrencyAmount {
        <#code#>
    }
    
    public func getDefaultWallet() async throws -> Crypto_lunarisCryptoWallet? {
        <#code#>
    }
    
    public func getTransactionHistory(address: String, offset: Int32, count: Int32, type: Crypto_lunarisTransactionType) async throws -> [Crypto_lunarisCryptoTransaction] {
        <#code#>
    }
    
    public func getTransactionStatus(txHash: String) async throws -> Crypto_lunarisTransactionStatus? {
        <#code#>
    }
    
    public func resolveRecipient(value: String) async throws -> Crypto_lunarisCryptoAccount? {
        <#code#>
    }
    
    public func restoreWallet(phrase: String, password: String?) async throws -> Crypto_lunarisCryptoWallet {
        <#code#>
    }
    
    public func revealSeedPhrase(address: String, password: String?) async throws -> String {
        <#code#>
    }
    
    public func send(origin: String, password: String?, target: String, amount: Util_sharedCurrencyAmount) async throws -> Crypto_lunarisSendResult {
        <#code#>
    }
    
    public func close() {
        <#code#>
    }
    
    private func getWeb3() async throws -> Web3 {
        if let cached = web3 {
            return cached
        }
        let infuraURL = URL(string: "https://polygon-rpc.com")!
        let new = try await Web3.new(infuraURL)
        
        web3 = new
        
        return new
    }
}

@objc public class SwiftCryptoWalletManagerFactory: NSObject, IosCryptoWalletManagerFactory {
    
    public func create(
        cryptoWalletAddressProvider: Crypto_lunarisCryptoWalletAddressProvider,
        polygonRpcUrl: String,
        walletDirectoryPath: String,
        cryptoWalletRepository: Crypto_lunarisCryptoWalletRepository,
        clock: Kotlinx_datetimeClock,
        currency: Util_sharedCurrency,
        currencyAddress: String,
        encryptor: Util_sharedAesEncryptor
    ) -> IosCryptoWalletManager {
       return SwiftCryptoWalletManager(
            walletDirectoryPath: walletDirectoryPath,
            cryptoWalletRepository: cryptoWalletRepository,
            currencyAddress: currencyAddress,
            encryptor: encryptor
       )
    }
}
