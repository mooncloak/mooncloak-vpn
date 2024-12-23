package com.mooncloak.vpn.app.shared.api.plan

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// TODO: Implement a cache: Perhaps an HTTP cache is enough?
public class PlansApiSource @Inject public constructor(
    private val api: MooncloakVpnServiceHttpApi
) : VPNServicePlansRepository {

    override suspend fun getAvailablePlans(): List<VPNServicePlan> =
        withContext(Dispatchers.IO) {
            api.getAvailablePlans().plans.filterIsInstance<VPNServicePlan>()
        }

    override suspend fun getPlan(id: String): VPNServicePlan =
        withContext(Dispatchers.IO) {
            (api.getPlan(id = id) as? VPNServicePlan) ?: error("Plan was NOT a VPNServicePlan instance.")
        }
}
