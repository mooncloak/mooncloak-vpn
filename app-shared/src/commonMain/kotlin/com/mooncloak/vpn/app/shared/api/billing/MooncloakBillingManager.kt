package com.mooncloak.vpn.app.shared.api.billing

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.update
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.plan.Plan
import com.mooncloak.vpn.app.shared.api.service.ServiceAccessDetails
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscription
import com.mooncloak.vpn.app.shared.api.service.ServiceTokens
import com.mooncloak.vpn.app.shared.api.service.ServiceTokensRepository
import com.mooncloak.vpn.app.shared.api.token.TransactionToken
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPersistentStateAPI::class)
internal class MooncloakBillingManager @Inject internal constructor(
    private val api: MooncloakVpnServiceHttpApi,
    private val subscriptionStorage: SubscriptionStorage,
    private val serviceAccessDetailsRepository: ServiceTokensRepository
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

    override suspend fun purchasePlan(plan: Plan) {
        val invoice = withContext(Dispatchers.IO) {
            api.getPaymentInvoice(
                planId = plan.id,
                token = subscriptionStorage.tokens.current.value?.accessToken
            )
        }

        // TODO: Display the UI for the invoice

        // TODO: Check the invoice payment status

        // TODO: Invoke the exchangePurchaseForServiceAccess function.

        TODO("Not yet implemented")
    }

    private suspend fun exchangePurchaseForServiceAccess(
        invoiceId: String,
        token: TransactionToken
    ): ServiceAccessDetails {
        val receipt = ProofOfPurchase(
            paymentProvider = PaymentProvider.Mooncloak,
            id = invoiceId,
            clientSecret = null,
            token = token
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
        val tokens = withContext(Dispatchers.IO) {
            api.exchangeToken(receipt = receipt)
        }

        serviceAccessDetailsRepository.add(tokens)

        return tokens
    }

    @OptIn(ExperimentalPersistentStateAPI::class)
    private suspend fun getSubscription(tokens: ServiceTokens): ServiceSubscription {
        val subscription = withContext(Dispatchers.IO) {
            api.getCurrentSubscription(token = tokens.accessToken)
        }

        subscriptionStorage.subscription.update(subscription)

        return subscription
    }
}
