package com.mooncloak.vpn.app.shared.api.plan

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: Implement a cache: Perhaps an HTTP cache is enough?
internal class DefaultPlansSource @Inject internal constructor(
    private val api: MooncloakVpnServiceHttpApi
) : PlansRepository {

    override suspend fun getAvailablePlans(): List<Plan> =
        withContext(Dispatchers.IO) {
            api.getAvailablePlans().plans
        }

    override suspend fun getPlan(id: String): Plan =
        withContext(Dispatchers.IO) {
            api.getAvailablePlans().plans.first { plan -> plan.id == id }
        }
}
