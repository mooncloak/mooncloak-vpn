package com.mooncloak.vpn.app.shared.api.plan

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi

public class ServicePlansApiSource @Inject public constructor(
    private val api: MooncloakVpnServiceHttpApi
) : ServicePlansRepository {

    override suspend fun getPlans(): List<Plan> =
        api.getAvailablePlans().plans

    override suspend fun getPlan(id: String): Plan =
        (api.getPlan(id = id) as? Plan) ?: error("Plan was NOT a VPNServicePlan instance.")
}
