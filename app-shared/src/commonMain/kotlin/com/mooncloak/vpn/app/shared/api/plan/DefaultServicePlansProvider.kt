package com.mooncloak.vpn.app.shared.api.plan

import com.mooncloak.kodetools.konstruct.annotations.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

public class DefaultServicePlansProvider @Inject public constructor(
    private val freshSource: ServicePlansRepository,
    private val cacheSource: ServicePlansDatabaseSource
) : ServicePlansProvider {

    override fun getPlansFlow(): Flow<List<ServicePlan>> =
        flow { emit(freshSource.getPlans()) }
            .onEach { plans -> cacheSource.insertAll(plans) }
            .onStart { emit(cacheSource.getPlans()) }
}
