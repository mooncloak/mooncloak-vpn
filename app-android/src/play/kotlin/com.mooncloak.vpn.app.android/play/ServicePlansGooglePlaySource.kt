package com.mooncloak.vpn.app.android.play

import com.android.billingclient.api.ProductDetails
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.textx.PlainText
import com.mooncloak.vpn.app.shared.api.plan.BillingProvider
import com.mooncloak.vpn.app.shared.api.plan.Plan
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansApiSource
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

internal class ServicePlansGooglePlaySource @Inject internal constructor(
    private val plansApiSource: ServicePlansApiSource,
    private val getGooglePlayProductsFromPlanIds: GetGooglePlayProductsFromPlanIdsUseCase
) : ServicePlansRepository {

    override suspend fun getPlans(): List<Plan> {
        val plans = plansApiSource.getPlans()
        val plansById = plans.associateBy { it.id }
        val googlePlayPlanIds = plans.filter { plan ->
            plan.provider == BillingProvider.GooglePlay
        }.map { plan -> plan.id }

        return getGooglePlayProductsFromPlanIds(planIds = googlePlayPlanIds).mapNotNull { product ->
            // Override the billing details with the Google Play Information, just in case they become out of sync.
            plansById[product.productId]?.copy(product)
        }.sortedBy { plan -> plan.price.amount }
    }

    override suspend fun getPlan(id: String): Plan =
        coroutineScope {
            val deferredPlan = async { plansApiSource.getPlan(id = id) }
            val product = runCatching { getGooglePlayProductsFromPlanIds(planIds = listOf(id)).first() }.getOrNull()

            if (product == null) {
                throw NoSuchElementException("No Google Play Product with id '$id'.")
            }

            return@coroutineScope deferredPlan.await().copy(product)
        }

    private fun Plan.copy(product: ProductDetails): Plan =
        this.copy(
            nickname = product.name,
            title = product.title,
            description = PlainText(value = product.description)
        )
}
