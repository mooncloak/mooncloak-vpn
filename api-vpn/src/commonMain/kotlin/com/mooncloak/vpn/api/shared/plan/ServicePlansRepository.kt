package com.mooncloak.vpn.api.shared.plan

import kotlinx.coroutines.CancellationException

public interface ServicePlansRepository {

    public suspend fun getPlans(): List<Plan>

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun getPlan(id: String): Plan

    public companion object
}

public suspend fun ServicePlansRepository.getPlanOrNull(id: String): Plan? =
    try {
        getPlan(id = id)
    } catch (_: NoSuchElementException) {
        null
    }
