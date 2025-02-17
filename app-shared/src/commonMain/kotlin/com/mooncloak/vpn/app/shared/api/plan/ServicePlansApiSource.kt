package com.mooncloak.vpn.app.shared.api.plan

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public class ServicePlansApiSource @Inject public constructor(
    private val api: MooncloakVpnServiceHttpApi
) : ServicePlansRepository {

    override suspend fun getPlans(): List<Plan> =
        withContext(Dispatchers.IO) {
            api.getAvailablePlans().plans
        }

    override suspend fun getPlan(id: String): Plan =
        withContext(Dispatchers.IO) {
            (api.getPlan(id = id) as? Plan) ?: error("Plan was NOT a VPNServicePlan instance.")
        }
}
