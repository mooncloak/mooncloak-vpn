package com.mooncloak.vpn.app.android.play

import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.util.coroutine.ApplicationCoroutineScope
import kotlinx.coroutines.launch

internal class GooglePlayPurchasesUpdatedListener @Inject internal constructor(
    private val coroutineScope: ApplicationCoroutineScope,
    private val exchangeGooglePlayPurchaseForServiceAccess: ExchangeGooglePlayPurchaseForServiceAccessUseCase
) : PurchasesUpdatedListener {

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        when {
            billingResult.responseCode == BillingResponseCode.OK && purchases != null -> {
                coroutineScope.launch {
                    purchases.forEach { purchase ->
                        exchangeGooglePlayPurchaseForServiceAccess(purchase)
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
