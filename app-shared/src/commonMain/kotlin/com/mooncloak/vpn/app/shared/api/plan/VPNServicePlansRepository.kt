package com.mooncloak.vpn.app.shared.api.plan

import kotlinx.coroutines.CancellationException
import kotlin.jvm.Throws

public interface VPNServicePlansRepository {

    public suspend fun getAvailablePlans(): List<VPNServicePlan>

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun getPlan(id: String): VPNServicePlan

    public companion object
}

public suspend fun VPNServicePlansRepository.getPlanOrNull(id: String): VPNServicePlan? =
    try {
        getPlan(id = id)
    } catch (_: NoSuchElementException) {
        null
    }
