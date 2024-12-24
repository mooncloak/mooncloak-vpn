package com.mooncloak.vpn.app.shared.api.plan

import com.mooncloak.kodetools.konstruct.annotations.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

public class DefaultVPNServicePlansProvider @Inject public constructor(
    private val freshSource: VPNServicePlansRepository,
    private val cacheSource: VPNServicePlansDatabaseSource
) : VPNServicePlansProvider {

    override fun getPlansFlow(): Flow<List<VPNServicePlan>> =
        flow { emit(freshSource.getPlans()) }
            .onEach { plans -> cacheSource.insertAll(plans) }
            .onStart { emit(cacheSource.getPlans()) }
}
