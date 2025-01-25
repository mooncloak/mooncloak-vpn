package com.mooncloak.vpn.app.android.play

import android.app.Activity
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.QueryPurchasesParams
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.billing.BillingManager
import com.mooncloak.vpn.app.shared.api.plan.Plan
import com.mooncloak.vpn.app.shared.di.PresentationScoped
import com.mooncloak.vpn.app.shared.util.coroutine.PresentationCoroutineScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlin.jvm.Throws

@PresentationScoped
internal class GooglePlayBillingManager @Inject internal constructor(
    billingClientProvider: BillingClientProvider,
    private val coroutineScope: PresentationCoroutineScope,
    private val activity: Activity,
    private val exchangeGooglePlayPurchaseForServiceAccess: ExchangeGooglePlayPurchaseForServiceAccessUseCase,
    private val launchGooglePlayBillingPurchasePlan: LaunchGooglePlayBillingPurchasePlanUseCase
) : BillingManager {

    public override var isActive: Boolean = false
        private set

    private val billingClient = billingClientProvider.get()

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
                                    exchangeGooglePlayPurchaseForServiceAccess(purchase)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

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
    override suspend fun purchasePlan(plan: Plan) {
        launchGooglePlayBillingPurchasePlan(
            plan = plan,
            activity = activity
        )
    }
}
