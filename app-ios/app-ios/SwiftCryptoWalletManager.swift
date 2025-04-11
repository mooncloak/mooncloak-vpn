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

@objc public class SwiftCryptoWalletManager: IosCryptoWalletManager {
    
    private var web3: Web3?
    private var wallets: [String: BIP32Keystore] = [:] // In-memory wallet store
    
    private let cryptoWalletAddressProvider: Crypto_lunarisCryptoWalletAddressProvider
    private let polygonRpcUrl: String
    private let walletDirectoryPath: String
    private let cryptoWalletRepository: Crypto_lunarisCryptoWalletRepository
    private let currency: Util_sharedCurrency
    private let currencyAddress: String
    private let encryptor: Util_sharedAesEncryptor
    
    override public init(
        cryptoWalletAddressProvider: Crypto_lunarisCryptoWalletAddressProvider,
        polygonRpcUrl: String,
        walletDirectoryPath: String,
        cryptoWalletRepository: Crypto_lunarisCryptoWalletRepository,
        clock: Kotlinx_datetimeClock,
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
        
        super.init(
            cryptoWalletAddressProvider: cryptoWalletAddressProvider,
            polygonRpcUrl: polygonRpcUrl,
            walletDirectoryPath: walletDirectoryPath,
            cryptoWalletRepository: cryptoWalletRepository,
            clock: clock,
            currency: currency,
            currencyAddress: currencyAddress,
            encryptor: encryptor
        )
    }
    
    public override func createWallet(password: String?) async throws -> Crypto_lunarisCryptoWallet {
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
        
        // Store keystore file
        let walletFileName = "\(address).json"
        let walletFileURL = URL(fileURLWithPath: walletDirectoryPath).appendingPathComponent(walletFileName)
        try keystore.serialize()?.write(to: walletFileURL)
        
        // Cache keystore
        wallets[address] = keystore
        
        // Encrypt phrase using common code’s encryptor
        let encryptedPhrase = try await super.encryptPhrase(phrase: mnemonic, password: password)
        
        // Use common code’s createAndStoreWallet
        return try await super.createAndStoreWallet(
            fileName: walletFileName,
            address: address,
            currency: currency,
            phrase: encryptedPhrase
        )
    }
    
    public override func estimateGas(origin: String, target: String, amount: Util_sharedCurrencyAmount) async throws -> Util_sharedCurrencyAmount? {
        return nil
    }
    
    public override func getBalance(address: String) async throws -> Util_sharedCurrencyAmount {
        let web3 = try await getWeb3()
        let ethAddress = EthereumAddress(address)!
        let contractAddress = EthereumAddress(currencyAddress)!
        
        let tokenContract = web3.contract(Web3.Utils.erc20ABI, at: contractAddress)!
        
        guard let readOp = tokenContract.createReadOperation("balanceOf", parameters: [ethAddress.address]) else {
            throw NSError(domain: "WalletError", code: -1, userInfo: [NSLocalizedDescriptionKey: "Failed to create balanceOf operation"])
        }
        readOp.transaction.from = ethAddress
        
        let response = try await readOp.callContractMethod()
        
        guard let balance = response["0"] as? BigUInt else {
            throw NSError(domain: "WalletError", code: -1, userInfo: [NSLocalizedDescriptionKey: "Failed to get balance"])
        }
        
        let balanceString = balance.description
        let balanceNumber = NSDecimalNumber(string: balanceString)
        
        return createLunarisAmountFromMinorUnit(value: balanceNumber)
    }
    
    public override func getDefaultWallet() async throws -> Crypto_lunarisCryptoWallet? {
        try await super.getDefaultWallet()
    }
    
    public override func getTransactionHistory(address: String, offset: Int32, count: Int32, type: Crypto_lunarisTransactionType) async throws -> [Crypto_lunarisCryptoTransaction] {
        fatalError("Not Implemented")
    }
    
    public override func getTransactionStatus(txHash: String) async throws -> Crypto_lunarisTransactionStatus? {
        let web3 = try await getWeb3()
        
        // Convert txHash string to Data
        guard let txHashData = Data.fromHex(txHash) else {
            return nil // Invalid hash format
        }
        
        // Fetch transaction receipt
        let receipt = try await web3.eth.transactionReceipt(txHashData)
        switch receipt.status {
        case .ok:
            return Crypto_lunarisTransactionStatus.confirmed
        case .failed:
            return Crypto_lunarisTransactionStatus.failed
        case .notYetProcessed:
            return Crypto_lunarisTransactionStatus.pending
        }
    }
    
    public override func resolveRecipient(value: String) async throws -> Crypto_lunarisCryptoAccount? {
        let web3 = try await getWeb3()
        guard let ens = ENS(web3: web3) else {
            throw NSError(domain: "WalletError", code: -1, userInfo: [NSLocalizedDescriptionKey: "Failed to resolve recipient"])
        }
        
        // Ethereum address regex (matches "^0x[a-fA-F0-9]{40}$")
        let addressRegex = try NSRegularExpression(pattern: "^0x[a-fA-F0-9]{40}$", options: [])
        let range = NSRange(value.startIndex..<value.endIndex, in: value)
        
        // 1. Check if input is a valid Ethereum address
        if addressRegex.firstMatch(in: value, options: [], range: range) != nil {
            let address = EthereumAddress(value)!
            
            do {
                let name = try await ens.getName(forNode: address.address)
                
                return Crypto_lunarisCryptoAccount(address: value, name: name)
            } catch {
                // Reverse lookup failed, return account with address only
                return Crypto_lunarisCryptoAccount(address: value, name: nil)
            }
        }
        
        // 2. Check if input is a full ENS name (e.g., "chris.eth")
        if value.lowercased().hasSuffix(".eth") {
            do {
                let address = try await ens.getAddress(forNode: value.lowercased())
                
                return Crypto_lunarisCryptoAccount(address: address.address, name: value.lowercased())
            } catch {
                // ENS resolution failed, move to next check
            }
        }
        
        // 3. Test input + ".eth" (e.g., "chris" -> "chris.eth")
        let ensCandidate = "\(value.lowercased()).eth"
        do {
            let address = try await ens.getAddress(forNode: ensCandidate)
            
            return Crypto_lunarisCryptoAccount(address: address.address, name: ensCandidate)
        } catch {
            // No valid resolution
        }
        
        return nil
    }
    
    public override func restoreWallet(phrase: String, password: String?) async throws -> Crypto_lunarisCryptoWallet {
        // Validate seed phrase (12 or 24 words)
        let words = phrase.trimmingCharacters(in: .whitespaces).split(separator: " ")
        guard words.count == 12 || words.count == 24 else {
            throw NSError(domain: "WalletError", code: -1, userInfo: [NSLocalizedDescriptionKey: "Invalid seed phrase: must be 12 or 24 words"])
        }
        
        // Restore wallet with empty password for mnemonic, as per Android logic
        let keystore = try BIP32Keystore(
            mnemonics: phrase,
            password: "", // Empty string, not used as 25th word
            mnemonicsPassword: "",
            language: .english,
            prefixPath: "m/44'/60'/0'/0" // Standard BIP-44 path for Ethereum/Polygon
        )!
        
        guard let address = keystore.addresses?.first?.address else {
            throw NSError(domain: "WalletError", code: -1, userInfo: [NSLocalizedDescriptionKey: "Failed to get wallet address"])
        }
        
        // Generate wallet file with provided password (or empty string)
        let walletFileName = "\(address).json"
        let walletFileURL = URL(fileURLWithPath: walletDirectoryPath).appendingPathComponent(walletFileName)
        try keystore.serialize()?.write(to: walletFileURL)
        
        // Store in memory
        wallets[address] = keystore
        
        // Encrypt the phrase
        let encryptedPhrase = try await encryptPhrase(phrase: phrase, password: password)
        
        // Create and store the wallet
        return try await createAndStoreWallet(
            fileName: walletFileName,
            address: address,
            currency: currency,
            phrase: encryptedPhrase
        )
    }
    
    public override func revealSeedPhrase(address: String, password: String?) async throws -> String {
        return try await super.revealSeedPhrase(address: address, password: password)
    }
    
    public override func send(origin: String, password: String?, target: String, amount: Util_sharedCurrencyAmount) async throws -> Crypto_lunarisSendResult {
        let web3 = try await getWeb3()
            
        // Load wallet from repository and keystore
        guard let wallet = try await super.getWalletByAddressOrNull(address: origin) else {
            return super.createFailureSendResult(errorMessage: "Wallet not found for address: \(origin)")
        }
        
        let walletFileURL = URL(fileURLWithPath: wallet.location)
        guard let keystore = BIP32Keystore(try Data(contentsOf: walletFileURL)) else {
            return super.createFailureSendResult(errorMessage: "Failed to load keystore for wallet: \(origin)")
        }
        
        let fromAddress = EthereumAddress(origin)!
        let toAddress = EthereumAddress(target)!
        let contractAddress = EthereumAddress(currencyAddress)!
        let amountWei = BigUInt(exactly: amount.toMinorUnits())!
        
        // Prepare ERC-20 transfer operation
        let tokenContract = web3.contract(Web3.Utils.erc20ABI, at: contractAddress)!
        guard let transferOp = tokenContract.createWriteOperation("transfer", parameters: [toAddress.address, amountWei]) else {
            return super.createFailureSendResult(errorMessage: "Failed to create transfer operation")
        }
        
        // Set transaction parameters
        transferOp.transaction.from = fromAddress
        
        // Fetch nonce
        let nonce = try await web3.eth.getTransactionCount(for: fromAddress, onBlock: .latest)
        transferOp.transaction.nonce = nonce
        
        // Fetch gas price (MATIC)
        let gasPrice = try await web3.eth.gasPrice()
        transferOp.transaction.gasPrice = gasPrice
        
        // Estimate gas limit (or fallback to default)
        do {
            let gasEstimate = try await web3.eth.estimateGas(for: transferOp.transaction)
            transferOp.transaction.gasLimit = gasEstimate
        } catch {
            transferOp.transaction.gasLimit = BigUInt(65000) // Fallback for Polygon
        }
        
        // Chain ID for Polygon (137)
        // TODO: Use currency.chainId
        transferOp.transaction.chainID = BigUInt(exactly: 137)
        
        // Send the transaction
        do {
            let result = try await transferOp.writeToChain(password: password ?? "")
            let txHash = result.hash
            guard let txHashData = Data.fromHex(txHash) else {
                return super.createFailureSendResult(errorMessage: "Invalid transaction hash format: \(txHash)")
            }
            
            // Check transaction receipt
            let receipt = try await web3.eth.transactionReceipt(txHashData)
                if receipt.status == .ok {
                    let gasUsed = receipt.gasUsed
                    return super.createSuccessSendResult(txHash: txHash, gasUsed: gasUsed)
                } else {
                    return super.createPendingSendResult(txHash: txHash)
                }
        } catch {
            return super.createFailureSendResult(errorMessage: "Send transaction failed: \(error.localizedDescription)")
        }
    }
    
    public override func close() {
        web3 = nil
        wallets.removeAll()
    }
    
    private func getWeb3() async throws -> Web3 {
        if let cached = web3 {
            return cached
        }
        
        let infuraURL = URL(string: polygonRpcUrl)!
        let new = try await Web3.new(infuraURL)
        
        web3 = new
        
        return new
    }
    
    private func loadKeystore(address: String) -> BIP32Keystore? {
        let walletFileURL = URL(fileURLWithPath: walletDirectoryPath).appendingPathComponent("\(address).json")
        
        guard let data = try? Data(contentsOf: walletFileURL),
              let keystore = BIP32Keystore(data) else {
            return nil
        }
        
        return keystore
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
            clock: clock,
            currency: currency,
            currencyAddress: currencyAddress,
            encryptor: encryptor
       )
    }
}
