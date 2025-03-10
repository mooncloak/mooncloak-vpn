package com.mooncloak.vpn.app.android.play

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.consumePurchase
import com.android.billingclient.api.queryProductDetails
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.vpn.api.shared.billing.BillingManager
import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.api.shared.plan.Price
import com.mooncloak.vpn.app.shared.di.PresentationScoped
import com.mooncloak.vpn.util.shared.coroutine.PresentationCoroutineScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.jvm.Throws

@PresentationScoped
internal class GooglePlayBillingManager @Inject internal constructor(
    private val activity: Activity,
    private val coroutineScope: PresentationCoroutineScope,
    private val exchangeGooglePlayPurchaseForServiceAccess: ExchangeGooglePlayPurchaseForServiceAccessUseCase
) : BillingManager {

    public override var isActive: Boolean = false
        private set

    private val prices = mutableMapOf<String, Price>()

    private val connectionStateListener = object : BillingClientStateListener {

        override fun onBillingServiceDisconnected() {
            if (isActive) {
                billingClient.startConnection(this)
            }
        }

        override fun onBillingSetupFinished(billingResult: BillingResult) {
            if (billingResult.responseCode == BillingResponseCode.OK) {
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
                                    handlePurchase(purchase)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        coroutineScope.launch {
            val result = when {
                billingResult.responseCode == BillingResponseCode.OK && purchases != null -> {
                    purchases.forEach { purchase ->
                        handlePurchase(purchase)
                    }

                    com.mooncloak.vpn.api.shared.billing.BillingResult.Success(
                        plans = emptyList() // TODO: Get plan info?
                    )
                }

                billingResult.responseCode == BillingResponseCode.USER_CANCELED -> com.mooncloak.vpn.api.shared.billing.BillingResult.Cancelled()

                else -> com.mooncloak.vpn.api.shared.billing.BillingResult.Failure(
                    code = billingResult.responseCode,
                    message = billingResult.debugMessage
                )
            }

            continuation?.resume(result)
        }
    }

    private val billingClient = BillingClient.newBuilder(activity.applicationContext)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enablePrepaidPlans()
                .enableOneTimeProducts()
                .build()
        )
        .build()

    private var continuation: Continuation<com.mooncloak.vpn.api.shared.billing.BillingResult>? = null

    override fun start() {
        if (!isActive) {
            isActive = true

            billingClient.startConnection(connectionStateListener)
        }
    }

    override fun cancel() {
        if (isActive) {
            isActive = false

            billingClient.endConnection()
        }
    }

    @Throws(IllegalStateException::class, CancellationException::class)
    override suspend fun purchase(plan: Plan): com.mooncloak.vpn.api.shared.billing.BillingResult {
        // Store the price so that when the purchase listener is reached we can load and store that data for future use.
        prices[plan.id] = plan.price

        val product = getProductDetails(planIds = listOf(plan.id)).first()

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                .setProductDetails(product)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)

        return if (billingResult.responseCode != BillingResponseCode.OK) {
            com.mooncloak.vpn.api.shared.billing.BillingResult.Failure(
                code = billingResult.responseCode,
                message = billingResult.debugMessage,
                plans = listOf(plan)
            )
        } else {
            suspendCoroutine { continuation ->
                this.continuation = continuation
            }
        }
    }

    @Throws(IllegalStateException::class, CancellationException::class)
    internal suspend fun getProductDetails(planIds: List<String>): List<ProductDetails> {
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

    private suspend fun handlePurchase(purchase: Purchase) {
        exchangeGooglePlayPurchaseForServiceAccess(
            purchase = purchase,
            price = purchase.products.firstOrNull()
                ?.let { planId -> prices[planId] }
                ?: prices[purchase.orderId]
        )

        val consumedResult = consumePurchase(token = purchase.purchaseToken)

        if (consumedResult.isFailure) {
            LogPile.error(
                message = "Error consuming purchase.",
                cause = consumedResult.exceptionOrNull()
            )
        }
    }

    private suspend fun consumePurchase(token: String): Result<Unit> {
        val result: Result<Unit>

        // After we retrieve the tokens and subscription information, we need to consume the purchase, so that plans
        // can be purchased again. If we don't do this, the user can never buy the same plan again.
        withContext(Dispatchers.IO) {
            result = runCatching {
                billingClient.consumePurchase(
                    ConsumeParams.newBuilder()
                        .setPurchaseToken(token)
                        .build()
                )
            }
        }

        return result
    }
}
