package com.mooncloak.vpn.app.shared.api.repository

import com.mooncloak.vpn.app.shared.api.Plan
import kotlinx.coroutines.CancellationException
import kotlin.jvm.Throws

public interface PlansRepository {

    public suspend fun getAvailablePlans(): List<Plan>

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun getPlan(id: String): Plan

    public companion object
}

public suspend fun PlansRepository.getPlanOrNull(id: String): Plan? =
    try {
        getPlan(id = id)
    } catch (_: NoSuchElementException) {
        null
    }
