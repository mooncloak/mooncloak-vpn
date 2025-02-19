package com.mooncloak.vpn.app.android.play

import com.android.billingclient.api.Purchase
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.billing.ProofOfPurchase
import com.mooncloak.vpn.app.shared.api.billing.ServicePurchaseReceiptRepository
import com.mooncloak.vpn.app.shared.api.billing.usecase.ExchangeProofOfPurchaseForServiceTokensUseCase
import com.mooncloak.vpn.app.shared.api.billing.usecase.GetCurrentSubscriptionUseCase
import com.mooncloak.vpn.app.shared.api.plan.BillingProvider
import com.mooncloak.vpn.app.shared.api.plan.Plan
import com.mooncloak.vpn.app.shared.api.service.ServiceAccessDetails
import com.mooncloak.vpn.app.shared.api.token.TransactionToken
import kotlinx.datetime.Instant

internal class ExchangeGooglePlayPurchaseForServiceAccessUseCase @Inject internal constructor(
    private val servicePurchaseReceiptRepository: ServicePurchaseReceiptRepository,
    private val exchangeProofOfPurchaseForServiceTokens: ExchangeProofOfPurchaseForServiceTokensUseCase,
    private val getCurrentSubscription: GetCurrentSubscriptionUseCase
) {

    internal suspend operator fun invoke(
        purchase: Purchase,
        plan: Plan? = null
    ): ServiceAccessDetails {
        val proofOfPurchase = ProofOfPurchase(
            paymentProvider = BillingProvider.GooglePlay,
            orderId = purchase.orderId,
            productIds = purchase.products,
            clientSecret = null,
            token = TransactionToken(value = purchase.purchaseToken)
        )

        // Store the purchase receipt locally on device so that we can always look it up later if needed.
        servicePurchaseReceiptRepository.add(
            orderId = purchase.orderId,
            planIds = purchase.products,
            purchased = Instant.fromEpochMilliseconds(purchase.purchaseTime),
            provider = BillingProvider.GooglePlay,
            subscription = false,
            clientSecret = null,
            token = TransactionToken(value = purchase.purchaseToken),
            signature = purchase.signature,
            quantity = purchase.quantity,
            price = plan?.price
        )

        val tokens = exchangeProofOfPurchaseForServiceTokens(proofOfPurchase)
        val subscription = getCurrentSubscription(tokens)

        val accessDetails = ServiceAccessDetails(
            tokens = tokens,
            subscription = subscription
        )

        return accessDetails
    }
}
