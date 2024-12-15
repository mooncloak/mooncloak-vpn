package com.mooncloak.vpn.app.shared.api.source

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.Plan
import com.mooncloak.vpn.app.shared.api.repository.PlansRepository
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
