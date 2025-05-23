package com.mooncloak.vpn.app.shared.api.plan

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.api.shared.plan.ServicePlansRepository

public class ServicePlansApiSource @Inject public constructor(
    private val api: VpnServiceApi
) : ServicePlansRepository {

    override suspend fun getPlans(): List<Plan> =
        api.getAvailablePlans().plans
            .sortedBy { plan -> plan.price.amount }

    override suspend fun getPlan(id: String): Plan =
        (api.getPlan(id = id) as? Plan) ?: error("Plan was NOT a VPNServicePlan instance.")
}
