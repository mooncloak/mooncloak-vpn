package com.mooncloak.vpn.app.android.play

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.queryProductDetails
import com.mooncloak.kodetools.konstruct.annotations.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class GetGooglePlayProductsFromPlanIdsUseCase @Inject internal constructor(
    private val billingClientProvider: BillingClientProvider
) {

    @Throws(IllegalStateException::class, CancellationException::class)
    internal suspend operator fun invoke(planIds: List<String>): List<ProductDetails> {
        val billingClient = billingClientProvider.get()

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
}
