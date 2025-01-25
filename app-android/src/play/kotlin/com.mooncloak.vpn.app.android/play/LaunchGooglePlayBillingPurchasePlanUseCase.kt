package com.mooncloak.vpn.app.android.play

import android.app.Activity
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingFlowParams
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.plan.Plan

internal class LaunchGooglePlayBillingPurchasePlanUseCase @Inject internal constructor(
    private val billingClientProvider: BillingClientProvider,
    private val getGooglePlayProductsFromPlanIds: GetGooglePlayProductsFromPlanIdsUseCase
) {

    internal suspend operator fun invoke(
        plan: Plan,
        activity: Activity
    ) {
        val billingClient = billingClientProvider.get()
        val product = getGooglePlayProductsFromPlanIds(planIds = listOf(plan.id)).first()

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

        if (billingResult.responseCode != BillingResponseCode.OK) {
            error("Unexpected error launching billing flow.")
        }
    }
}
