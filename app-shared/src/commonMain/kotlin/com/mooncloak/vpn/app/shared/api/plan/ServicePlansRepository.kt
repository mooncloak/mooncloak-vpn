package com.mooncloak.vpn.app.shared.api.plan

import kotlinx.coroutines.CancellationException
import kotlin.jvm.Throws

public interface ServicePlansRepository {

    public suspend fun getPlans(): List<ServicePlan>

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun getPlan(id: String): ServicePlan

    public companion object
}

public suspend fun ServicePlansRepository.getPlanOrNull(id: String): ServicePlan? =
    try {
        getPlan(id = id)
    } catch (_: NoSuchElementException) {
        null
    }
