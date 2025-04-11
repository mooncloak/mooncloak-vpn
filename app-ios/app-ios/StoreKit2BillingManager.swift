//
//  StoreKit2BillingManager.swift
//  app-ios
//
//  Created by Chris Keenan on 4/10/25.
//

import Foundation
import StoreKit
import app_shared

@objc public class StoreKit2BillingManager: Api_vpnIosBillingManager {
    
    private var updateListenerTask: Task<Void, Never>? = nil
    
    override public func startObservingTransactions() {
        updateListenerTask = Task.detached {
            for await result in Transaction.updates {
                switch result {
                case .verified(let transaction):
                    await self.processTransaction(transaction: transaction, planId: transaction.productID)
                case .unverified:
                    print("Unverified transaction: \(result)")
                }
            }
        }
    }
    
    override public func stopObservingTransactions() {
        updateListenerTask?.cancel()
        updateListenerTask = nil
    }
    
    override public func syncExistingTransactions() async throws {
        for await result in Transaction.currentEntitlements {
            switch result {
            case .verified(let transaction):
                await self.processTransaction(transaction: transaction, planId: transaction.productID)
            case .unverified:
                print("Unverified transaction: \(result)")
            }
        }
    }
    
    override public func getProducts(productIds: [String]) async throws -> [Api_vpnPlan] {
        let products = try await Product.products(for: productIds)
        
        return products.map { product in
            // Map subscription period
            let subscriptionValue = product.subscription?.subscriptionPeriod.value
            let subscriptionUnit: String? = {
                guard let unit = product.subscription?.subscriptionPeriod.unit else { return nil }
                switch unit {
                case .day: return "day"
                case .week: return "week"
                case .month: return "month"
                case .year: return "year"
                @unknown default: return nil
                }
            }()
            
            // Map trial period (introductory offer)
            let trialValue = product.subscription?.introductoryOffer?.period.value
            let trialUnit: String? = {
                guard let unit = product.subscription?.introductoryOffer?.period.unit else { return nil }
                switch unit {
                case .day: return "day"
                case .week: return "week"
                case .month: return "month"
                case .year: return "year"
                @unknown default: return nil
                }
            }()
            
            let priceMajor = NSDecimalNumber(decimal: product.price).doubleValue
            
            return self.createPlan(
                id: product.id,
                priceMajor: priceMajor,
                currencyCode: product.priceFormatStyle.currencyCode,
                formattedPrice: product.displayPrice,
                displayName: product.displayName,
                description: product.description,
                autoRenews: product.type == .autoRenewable,
                subscriptionValue: subscriptionValue.map { KotlinInt(int: Int32($0)) },
                subscriptionUnit: subscriptionUnit,
                trialValue: trialValue.map { KotlinInt(int: Int32($0)) },
                trialUnit: trialUnit
            )
        }
    }
    
    override public func purchaseProduct(plan: Api_vpnPlan) async throws -> Api_vpnBillingResult {
        let products = try await Product.products(for: [plan.id as String])
        
        guard let product = products.first else {
            return super.createFailureResult(code: nil, message: "Product not found", plans: [plan], cause: nil)
        }
        
        let result = try await product.purchase()
        switch result {
        case .success(let verification):
            if case .verified(let transaction) = verification {
                await self.processTransaction(transaction: transaction, planId: transaction.productID)
                
                return super.createSuccessResult(code: nil, message: nil, plans: [plan])
            } else {
                return super.createFailureResult(code: nil, message: "Unverified transaction", plans: [plan], cause: nil)
            }
        case .userCancelled:
            return super.createCancelledResult(code: nil, message: nil, plans: [plan])
        case .pending:
            return super.createFailureResult(code: nil, message: "Purchase pending", plans: [plan], cause: nil)
        @unknown default:
            return super.createFailureResult(code: nil, message: "Unknown error", plans: [plan], cause: nil)
        }
    }
    
    private func processTransaction(transaction: Any, planId: String) async {
        if let transaction = transaction as? Transaction {
            guard let jsonString = String(data: transaction.jsonRepresentation, encoding: .utf8) else {
                fatalError("Unexpected Transaction token format.")
            }
            
            super.handleResult(
                token: super.createTransactionToken(value: jsonString),
                productIds: [planId],
                state: nil
            )
            
            await transaction.finish()
        }
    }
}

@objc public class StoreKit2BillingManagerFactory: NSObject, Api_vpnIosBillingManagerFactory {
    
    public func create(coroutineScope: Kotlinx_coroutines_coreCoroutineScope) -> Api_vpnIosBillingManager {
        return StoreKit2BillingManager(coroutineScope: coroutineScope)
    }
}
