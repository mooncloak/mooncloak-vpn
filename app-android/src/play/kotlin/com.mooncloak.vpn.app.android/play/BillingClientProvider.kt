package com.mooncloak.vpn.app.android.play

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.PendingPurchasesParams
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.app.shared.util.ApplicationContext
import com.mooncloak.vpn.app.shared.util.coroutine.ApplicationCoroutineScope
import kotlinx.coroutines.launch

@Singleton
internal class BillingClientProvider @Inject internal constructor(
    private val coroutineScope: ApplicationCoroutineScope,
    private val context: ApplicationContext,
    private val exchangeGooglePlayPurchaseForServiceAccess: ExchangeGooglePlayPurchaseForServiceAccessUseCase,
    private val consumePurchase: ConsumePurchaseUseCase
) {

    private var cached: BillingClient? = null

    internal fun get(): BillingClient {
        cached?.let { return it }

        val billingClient = BillingClient.newBuilder(context.applicationContext)
            .setListener { billingResult, purchases ->
                when {
                    billingResult.responseCode == BillingResponseCode.OK && purchases != null -> {
                        coroutineScope.launch {
                            purchases.forEach { purchase ->
                                exchangeGooglePlayPurchaseForServiceAccess(purchase)

                                consumePurchase(
                                    client = this@BillingClientProvider.get(),
                                    token = purchase.purchaseToken
                                )
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
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enablePrepaidPlans()
                    .enableOneTimeProducts()
                    .build()
            )
            .build()

        cached = billingClient

        return billingClient
    }
}
