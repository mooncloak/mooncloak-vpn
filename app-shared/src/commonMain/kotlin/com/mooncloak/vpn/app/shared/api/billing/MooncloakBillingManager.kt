package com.mooncloak.vpn.app.shared.api.billing

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.billing.BillingManager
import com.mooncloak.vpn.api.shared.billing.BillingResult
import com.mooncloak.vpn.api.shared.billing.MutableServicePurchaseReceiptRepository
import com.mooncloak.vpn.api.shared.billing.ProofOfPurchase
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.plan.BillingProvider
import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.api.shared.service.ServiceAccessDetails
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.api.shared.service.ServiceTokens
import com.mooncloak.vpn.api.shared.service.ServiceTokensRepository
import com.mooncloak.vpn.api.shared.token.TransactionToken
import com.mooncloak.vpn.app.shared.settings.SubscriptionSettings
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

public class MooncloakBillingManager @Inject public constructor(
    private val api: VpnServiceApi,
    private val subscriptionStorage: SubscriptionSettings,
    private val serviceTokensRepository: ServiceTokensRepository,
    private val servicePurchaseReceiptRepository: MutableServicePurchaseReceiptRepository,
    private val clock: Clock
) : BillingManager {

    override var isActive: Boolean = false
        private set

    override fun start() {
        if (!isActive) {
            isActive = true
        }
    }

    override fun cancel() {
        isActive = false
    }

    override suspend fun purchase(plan: Plan): BillingResult {
        val invoice = withContext(Dispatchers.PlatformIO) {
            api.getPaymentInvoice(
                planId = plan.id,
                token = serviceTokensRepository.getLatest()?.accessToken
            )
        }

        // TODO: Display the UI for the invoice

        // TODO: Check the invoice payment status

        // TODO: Invoke the exchangePurchaseForServiceAccess function.

        TODO("Not yet implemented")
    }

    private suspend fun exchangePurchaseForServiceAccess(
        planId: String,
        invoiceId: String,
        token: TransactionToken
    ): ServiceAccessDetails {
        val receipt = ProofOfPurchase(
            paymentProvider = BillingProvider.Mooncloak,
            orderId = invoiceId,
            clientSecret = null,
            token = token
        )

        // Store the purchase receipt locally on device so that we can always look it up later if needed.
        servicePurchaseReceiptRepository.add(
            planIds = listOf(planId),
            invoiceId = invoiceId,
            purchased = clock.now(),
            provider = BillingProvider.GooglePlay,
            subscription = false,
            clientSecret = null,
            token = token,
            signature = null,
            quantity = null
        )

        val tokens = getTokens(receipt)
        val subscription = getSubscription(tokens)
        val accessDetails = ServiceAccessDetails(
            tokens = tokens,
            subscription = subscription
        )

        return accessDetails
    }

    private suspend fun getTokens(receipt: ProofOfPurchase): ServiceTokens {
        val tokens = api.exchangeToken(receipt = receipt)

        serviceTokensRepository.add(tokens)

        return tokens
    }

    private suspend fun getSubscription(tokens: ServiceTokens): ServiceSubscription {
        val subscription = api.getCurrentSubscription(token = tokens.accessToken)

        subscriptionStorage.subscription.set(subscription)

        return subscription
    }
}
