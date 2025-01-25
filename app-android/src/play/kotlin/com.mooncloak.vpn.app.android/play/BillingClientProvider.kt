package com.mooncloak.vpn.app.android.play

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PendingPurchasesParams
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.app.shared.util.ApplicationContext

@Singleton
internal class BillingClientProvider @Inject internal constructor(
    private val context: ApplicationContext,
    private val purchasesUpdatedListener: GooglePlayPurchasesUpdatedListener
) {

    private var cached: BillingClient? = null

    internal fun get(): BillingClient {
        cached?.let { return it }

        val billingClient = BillingClient.newBuilder(context.applicationContext)
            .setListener(purchasesUpdatedListener)
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
