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
    
    private let cryptoWalletAddressProvider: Crypto_lunarisCryptoWalletAddressProvider
    private let polygonRpcUrl: String
    private let walletDirectoryPath: String
    private let cryptoWalletRepository: Crypto_lunarisCryptoWalletRepository
    private let currency: Util_sharedCurrency
    private let currencyAddress: String
    private let encryptor: Util_sharedAesEncryptor
    
    // Assuming these are passed in via a factory method similar to Android
    init(
        cryptoWalletAddressProvider: Crypto_lunarisCryptoWalletAddressProvider,
        polygonRpcUrl: String,
        walletDirectoryPath: String,
        cryptoWalletRepository: Crypto_lunarisCryptoWalletRepository,
        currency: Util_sharedCurrency,
        currencyAddress: String,
        encryptor: Util_sharedAesEncryptor
    ) {
        self.cryptoWalletAddressProvider = cryptoWalletAddressProvider
        self.polygonRpcUrl = polygonRpcUrl
        self.walletDirectoryPath = walletDirectoryPath
        self.cryptoWalletRepository = cryptoWalletRepository
        self.currency = currency
        self.currencyAddress = currencyAddress
        self.encryptor = encryptor
        super.init()
    }

    public func createWallet(password: String?) async throws -> Crypto_lunarisCryptoWallet {
        let mnemonic = try BIP39.generateMnemonics(bitsOfEntropy: 128, language: .english)!
        let keystore = try BIP32Keystore(
            mnemonics: mnemonic,
            password: password ?? "",
            mnemonicsPassword: "",
            language: .english,
            prefixPath: "m/44'/60'/0'/0"
        )!
        
        guard let address = keystore.addresses?.first?.address else {
            throw NSError(domain: "WalletError", code: -1, userInfo: [NSLocalizedDescriptionKey: "Failed to get wallet address"])
        }
        
        let walletFileURL = URL(fileURLWithPath: walletDirectoryPath).appendingPathComponent("\(address).json")
        try keystore.serialize()?.write(to: walletFileURL)
        
        wallets[address] = keystore
        
        let wallet = Crypto_lunarisCryptoWallet(
            address: address,
            createdAt: Kotlinx_datetimeClock.System.now(),
            updatedAt: Kotlinx_datetimeClock.System.now()
        )
        try await cryptoWalletRepository.store(wallet: wallet)
        
        return wallet
    }
    
    public func estimateGas(origin: String, target: String, amount: Util_sharedCurrencyAmount) async throws -> Util_sharedCurrencyAmount? {
        fatalError("Not Implemented")
    }
    
    public func getBalance(address: String) async throws -> Util_sharedCurrencyAmount {
        fatalError("Not Implemented")
    }
    
    public func getDefaultWallet() async throws -> Crypto_lunarisCryptoWallet? {
        fatalError("Not Implemented")
    }
    
    public func getTransactionHistory(address: String, offset: Int32, count: Int32, type: Crypto_lunarisTransactionType) async throws -> [Crypto_lunarisCryptoTransaction] {
        fatalError("Not Implemented")
    }
    
    public func getTransactionStatus(txHash: String) async throws -> Crypto_lunarisTransactionStatus? {
        fatalError("Not Implemented")
    }
    
    public func resolveRecipient(value: String) async throws -> Crypto_lunarisCryptoAccount? {
        fatalError("Not Implemented")
    }
    
    public func restoreWallet(phrase: String, password: String?) async throws -> Crypto_lunarisCryptoWallet {
        fatalError("Not Implemented")
    }
    
    public func revealSeedPhrase(address: String, password: String?) async throws -> String {
        fatalError("Not Implemented")
    }
    
    public func send(origin: String, password: String?, target: String, amount: Util_sharedCurrencyAmount) async throws -> Crypto_lunarisSendResult {
        fatalError("Not Implemented")
    }
    
    public func close() {
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
            cryptoWalletAddressProvider: cryptoWalletAddressProvider,
            polygonRpcUrl: polygonRpcUrl,
            walletDirectoryPath: walletDirectoryPath,
            cryptoWalletRepository: cryptoWalletRepository,
            currency: currency,
            currencyAddress: currencyAddress,
            encryptor: encryptor
       )
    }
}
