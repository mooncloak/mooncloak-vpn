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
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.billing.BillingManager
import com.mooncloak.vpn.app.shared.api.billing.PaymentProvider
import com.mooncloak.vpn.app.shared.api.plan.Plan
import com.mooncloak.vpn.app.shared.api.billing.PurchaseReceipt
import com.mooncloak.vpn.app.shared.api.service.ServiceAccessDetails
import com.mooncloak.vpn.app.shared.api.token.TransactionToken
import com.mooncloak.vpn.app.shared.api.plan.PlansRepository
import com.mooncloak.vpn.app.shared.api.service.ServiceAccessDetailsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.jvm.Throws

public class GooglePlayBillingManager @Inject internal constructor(
    private val context: Activity,
    private val plansRepository: PlansRepository,
    private val api: MooncloakVpnServiceHttpApi,
    private val serviceAccessDetailsRepository: ServiceAccessDetailsRepository
) : BillingManager {

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

    private val billingClient = BillingClient.newBuilder(context)
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

    public override fun start() {
        if (!isActive) {
            coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
            isActive = true

            billingClient.startConnection(connectionStateListener)
        }
    }

    public override fun cancel() {
        if (isActive) {
            isActive = false
            isClientReady = false

            billingClient.endConnection()
        }
    }

    @Throws(IllegalStateException::class, CancellationException::class)
    public override suspend fun getAvailablePlans(): List<Plan> {
        val plans = plansRepository.getAvailablePlans()
        val plansById = plans.associateBy { it.id }

        return getGooglePlayProducts(planIds = plans.map { plan -> plan.id }).mapNotNull { product ->
            // Override the billing details with the Google Play Information, just in case they become out of sync.
            plansById[product.productId]?.copy(
                nickname = product.name,
                title = product.title,
                description = product.description
            )
        }
    }

    @Throws(IllegalStateException::class, CancellationException::class)
    public override suspend fun purchasePlan(plan: Plan) {
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
                .setProductType(BillingClient.ProductType.SUBS)
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
        val receipt = PurchaseReceipt(
            paymentProvider = PaymentProvider.GooglePlay,
            id = purchase.orderId,
            clientSecret = null,
            token = TransactionToken(value = purchase.purchaseToken)
        )

        val tokens = withContext(Dispatchers.IO) {
            api.exchangeToken(receipt = receipt)
        }

        val subscription = withContext(Dispatchers.IO) {
            api.getCurrentSubscription(token = tokens.accessToken)
        }

        val accessDetails = ServiceAccessDetails(
            tokens = tokens,
            subscription = subscription
        )

        serviceAccessDetailsRepository.add(accessDetails)

        return accessDetails
    }
}
