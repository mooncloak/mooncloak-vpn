package com.mooncloak.vpn.app.shared.api.billing

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.billing.BillingManager
import com.mooncloak.vpn.api.shared.billing.BillingResult
import com.mooncloak.vpn.api.shared.billing.MutableServicePurchaseReceiptRepository
import com.mooncloak.vpn.api.shared.billing.ProofOfPurchase
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.plan.BillingProvider
import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.api.shared.service.ServiceAccessDetails
import com.mooncloak.vpn.api.shared.service.ServiceTokensRepository
import com.mooncloak.vpn.api.shared.token.TransactionToken
import com.mooncloak.vpn.app.shared.api.service.GetServiceSubscriptionForTokensUseCase
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

public class MooncloakBillingManager @Inject public constructor(
    private val api: VpnServiceApi,
    private val serviceTokensRepository: ServiceTokensRepository,
    private val servicePurchaseReceiptRepository: MutableServicePurchaseReceiptRepository,
    private val exchangeProofOfPurchaseForServiceTokens: ExchangeProofOfPurchaseForServiceTokensUseCase,
    private val getServiceSubscriptionForTokens: GetServiceSubscriptionForTokensUseCase,
    private val clock: Clock,
    private val uriHandler: UriHandler
) : BillingManager {

    private var continuation: CancellableContinuation<BillingResult>? = null

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
        val accessToken = serviceTokensRepository.getLatest()?.accessToken

        withContext(Dispatchers.PlatformIO) {
            suspendCancellableCoroutine<BillingResult> { continuation ->
                this@MooncloakBillingManager.continuation = continuation


            }
        }

        // TODO: Display the UI for the invoice

        // TODO: Check the invoice payment status

        // TODO: Invoke the exchangePurchaseForServiceAccess function.

        TODO("Not yet implemented")
    }

    override fun handleReceipt(token: TransactionToken, state: String?) {
        // TODO:
    }

    private suspend fun exchangePurchaseForServiceAccess(
        planId: String,
        invoiceId: String,
        token: TransactionToken
    ): ServiceAccessDetails {
        val proofOfPurchase = ProofOfPurchase(
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

        val tokens = exchangeProofOfPurchaseForServiceTokens(proofOfPurchase)
        val subscription = getServiceSubscriptionForTokens(tokens)

        val accessDetails = ServiceAccessDetails(
            tokens = tokens,
            subscription = subscription
        )

        return accessDetails
    }
}
