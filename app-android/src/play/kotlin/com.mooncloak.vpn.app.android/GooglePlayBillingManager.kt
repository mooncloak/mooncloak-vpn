package com.mooncloak.vpn.app.android

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.warning
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.update
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.billing.BillingManager
import com.mooncloak.vpn.app.shared.api.billing.PaymentProvider
import com.mooncloak.vpn.app.shared.api.plan.Plan
import com.mooncloak.vpn.app.shared.api.billing.ProofOfPurchase
import com.mooncloak.vpn.app.shared.api.billing.ServicePurchaseReceiptRepository
import com.mooncloak.vpn.app.shared.api.plan.ServicePlan
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansApiSource
import com.mooncloak.vpn.app.shared.api.service.ServiceAccessDetails
import com.mooncloak.vpn.app.shared.api.token.TransactionToken
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansRepository
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscription
import com.mooncloak.vpn.app.shared.api.service.ServiceTokens
import com.mooncloak.vpn.app.shared.api.service.ServiceTokensRepository
import com.mooncloak.vpn.app.shared.di.PresentationScoped
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlin.jvm.Throws

@PresentationScoped
internal class GooglePlayBillingManager @Inject internal constructor(
    private val context: Activity,
    private val plansApiSource: ServicePlansApiSource,
    private val api: MooncloakVpnServiceHttpApi,
    private val serviceTokensRepository: ServiceTokensRepository,
    private val subscriptionStorage: SubscriptionStorage,
    private val servicePurchaseReceiptRepository: ServicePurchaseReceiptRepository
) : BillingManager,
    ServicePlansRepository {

    public override var isActive: Boolean = false
        private set

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (isActive) {
            when {
                billingResult.responseCode == BillingResponseCode.OK && purchases != null -> {
                    coroutineScope.launch {
                        purchases.forEach { purchase ->
                            exchangePurchaseForServiceAccess(purchase)
                        }
                    }
                }

                billingResult.responseCode == BillingResponseCode.USER_CANCELED -> {
                    // TODO:
                }

                else -> {
                    // TODO:
                }
            }
        }
    }

    private val billingClient = BillingClient.newBuilder(context.applicationContext)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enablePrepaidPlans()
                .enableOneTimeProducts()
                .build()
        )
        .build()

    private val connectionStateListener = object : BillingClientStateListener {

        override fun onBillingServiceDisconnected() {
            isClientReady = false

            if (isActive) {
                billingClient.startConnection(this)
            }
        }

        override fun onBillingSetupFinished(billingResult: BillingResult) {
            if (billingResult.responseCode == BillingResponseCode.OK) {
                isClientReady = true

                // The BillingClient is ready. Query purchases to make sure there was none we missed. This is the
                // recommended approach from the Google Play Billing documentation.
                billingClient.queryPurchasesAsync(
                    QueryPurchasesParams.newBuilder()
                        .build()
                ) { purchasesResult, purchases ->
                    if (isActive) {
                        if (purchasesResult.responseCode == BillingResponseCode.OK) {
                            coroutineScope.launch {
                                purchases.forEach { purchase ->
                                    exchangePurchaseForServiceAccess(purchase)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private var isClientReady = false
    private lateinit var coroutineScope: CoroutineScope

    override fun start() {
        if (!isActive) {
            coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
            isActive = true

            billingClient.startConnection(connectionStateListener)
        }
    }

    override fun cancel() {
        if (isActive) {
            isActive = false
            isClientReady = false

            billingClient.endConnection()
        }
    }

    @Throws(IllegalStateException::class, CancellationException::class)
    override suspend fun getPlans(): List<ServicePlan> {
        val plans = plansApiSource.getPlans()
        val plansById = plans.associateBy { it.id }
        val googlePlayPlanIds = plans.filter { plan ->
            plan.provider == PaymentProvider.GooglePlay
        }.map { plan -> plan.id }

        return getGooglePlayProducts(planIds = googlePlayPlanIds).mapNotNull { product ->
            // Override the billing details with the Google Play Information, just in case they become out of sync.
            plansById[product.productId]?.copy(
                nickname = product.name,
                title = product.title,
                description = product.description
            )
        }
    }

    override suspend fun getPlan(id: String): ServicePlan =
        coroutineScope {
            val deferredPlan = async { plansApiSource.getPlan(id = id) }
            val product = runCatching { getGooglePlayProducts(planIds = listOf(id)).first() }.getOrNull()

            if (product == null) {
                throw NoSuchElementException("No Google Play Product with id '$id'.")
            }

            return@coroutineScope deferredPlan.await().copy(
                nickname = product.name,
                title = product.title,
                description = product.description
            )
        }

    @Throws(IllegalStateException::class, CancellationException::class)
    override suspend fun purchasePlan(plan: Plan) {
        val product = getGooglePlayProducts(planIds = listOf(plan.id)).first()

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                .setProductDetails(product)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val billingResult = billingClient.launchBillingFlow(context, billingFlowParams)

        if (billingResult.responseCode != BillingResponseCode.OK) {
            error("Unexpected error launching billing flow.")
        }
    }

    @Throws(IllegalStateException::class, CancellationException::class)
    private suspend fun getGooglePlayProducts(planIds: List<String>): List<ProductDetails> {
        val productFilters = planIds.map { id ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(id)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productFilters)
            .build()

        val result = withContext(Dispatchers.IO) {
            billingClient.queryProductDetails(params)
        }

        if (result.billingResult.responseCode == BillingResponseCode.OK) {
            return result.productDetailsList ?: emptyList()
        } else {
            throw IllegalStateException("Error retrieving plans. ${result.billingResult.responseCode}: ${result.billingResult.debugMessage}")
        }
    }

    private suspend fun exchangePurchaseForServiceAccess(purchase: Purchase): ServiceAccessDetails {
        val proofOfPurchase = ProofOfPurchase(
            paymentProvider = PaymentProvider.GooglePlay,
            id = purchase.orderId,
            clientSecret = null,
            token = TransactionToken(value = purchase.purchaseToken)
        )

        // Store the purchase receipt locally on device so that we can always look it up later if needed.
        servicePurchaseReceiptRepository.add(
            planId = purchase.orderId!!,
            purchased = Instant.fromEpochMilliseconds(purchase.purchaseTime),
            provider = PaymentProvider.GooglePlay,
            subscription = false,
            clientSecret = null,
            token = TransactionToken(value = purchase.purchaseToken),
            signature = purchase.signature,
            quantity = purchase.quantity
        )

        val tokens = getTokens(proofOfPurchase)
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

        serviceTokensRepository.add(tokens)

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
