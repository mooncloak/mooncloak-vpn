package com.mooncloak.vpn.app.shared.api.billing

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.vpn.api.shared.billing.BillingManager
import com.mooncloak.vpn.api.shared.billing.BillingResult
import com.mooncloak.vpn.api.shared.billing.MutableServicePurchaseReceiptRepository
import com.mooncloak.vpn.api.shared.billing.ProofOfPurchase
import com.mooncloak.vpn.api.shared.plan.BillingProvider
import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.api.shared.service.ServiceAccessDetails
import com.mooncloak.vpn.api.shared.service.ServiceTokensRepository
import com.mooncloak.vpn.api.shared.token.TransactionToken
import com.mooncloak.vpn.app.shared.api.service.GetServiceSubscriptionForTokensUseCase
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.coroutines.resume
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public class MooncloakBillingManager @Inject public constructor(
    private val applicationCoroutineScope: ApplicationCoroutineScope,
    private val serviceTokensRepository: ServiceTokensRepository,
    private val servicePurchaseReceiptRepository: MutableServicePurchaseReceiptRepository,
    private val exchangeProofOfPurchaseForServiceTokens: ExchangeProofOfPurchaseForServiceTokensUseCase,
    private val getServiceSubscriptionForTokens: GetServiceSubscriptionForTokensUseCase,
    private val clock: Clock,
    private val uriHandler: UriHandler
) : BillingManager {

    private var continuation: CancellableContinuation<BillingResult>? = null
    private var state: String? = null
    private var productId: String? = null

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

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun purchase(plan: Plan): BillingResult =
        try {
            // Generates a random state parameter value that is provided alongside the API request. This is similar to
            // the OAuth protocol to mitigate some security concerns.
            // TODO: Should probably use PKCE as well.
            state = Uuid.random().toHexString()

            val accessToken = serviceTokensRepository.getLatest()?.accessToken

            val uri = buildString {
                append("https://mooncloak.com/billing?product_id=${plan.id}")

                if (accessToken != null) {
                    append("&access_token=$accessToken")
                }

                if (state != null) {
                    append("&state=$state")
                }
            }

            uriHandler.openUri(uri)

            withContext(Dispatchers.PlatformIO) {
                suspendCancellableCoroutine { continuation ->
                    this@MooncloakBillingManager.continuation = continuation

                    // Continuation will be resumed in the handleReceipt function.
                }
            }
        } catch (e: Exception) {
            LogPile.error(
                message = "Error performing purchase.",
                cause = e
            )

            BillingResult.Failure(cause = e)
        }

    override fun handleReceipt(token: TransactionToken, state: String?) {
        applicationCoroutineScope.launch {
            try {
                if (this@MooncloakBillingManager.state == state && productId != null) {
                    val accessDetails = exchangePurchaseForServiceAccess(
                        planId = productId ?: "",
                        token = token
                    )

                    continuation?.resume(BillingResult.Success())
                } else {
                    continuation?.resume(BillingResult.Failure())
                }
            } catch (e: Exception) {
                LogPile.error(
                    message = "Error handling billing receipt.",
                    cause = e
                )

                continuation?.resume(BillingResult.Failure(cause = e))
            }
        }
    }

    private suspend fun exchangePurchaseForServiceAccess(
        planId: String,
        token: TransactionToken
    ): ServiceAccessDetails {
        val proofOfPurchase = ProofOfPurchase(
            paymentProvider = BillingProvider.Mooncloak,
            token = token
        )

        // Store the purchase receipt locally on device so that we can always look it up later if needed.
        servicePurchaseReceiptRepository.add(
            planIds = listOf(planId),
            invoiceId = null,
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
