package com.mooncloak.vpn.app.android.play

import com.android.billingclient.api.Purchase
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.billing.MutableServicePurchaseReceiptRepository
import com.mooncloak.vpn.api.shared.billing.ProofOfPurchase
import com.mooncloak.vpn.app.shared.api.billing.ExchangeProofOfPurchaseForServiceTokensUseCase
import com.mooncloak.vpn.app.shared.api.service.GetServiceSubscriptionForTokensUseCase
import com.mooncloak.vpn.api.shared.plan.BillingProvider
import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.api.shared.service.ServiceAccessDetails
import com.mooncloak.vpn.api.shared.token.TransactionToken
import kotlinx.datetime.Instant

internal class ExchangeGooglePlayPurchaseForServiceAccessUseCase @Inject internal constructor(
    private val servicePurchaseReceiptRepository: MutableServicePurchaseReceiptRepository,
    private val exchangeProofOfPurchaseForServiceTokens: ExchangeProofOfPurchaseForServiceTokensUseCase,
    private val getServiceSubscriptionForTokens: GetServiceSubscriptionForTokensUseCase
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
        val subscription = getServiceSubscriptionForTokens(tokens)

        val accessDetails = ServiceAccessDetails(
            tokens = tokens,
            subscription = subscription
        )

        return accessDetails
    }
}
