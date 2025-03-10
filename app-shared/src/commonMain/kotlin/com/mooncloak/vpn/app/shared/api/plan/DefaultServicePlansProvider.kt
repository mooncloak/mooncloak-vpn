package com.mooncloak.vpn.app.shared.api.plan

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.api.shared.plan.ServicePlansProvider
import com.mooncloak.vpn.api.shared.plan.ServicePlansRepository
import com.mooncloak.vpn.api.shared.plan.isAvailable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock

public class DefaultServicePlansProvider @Inject public constructor(
    private val freshSource: ServicePlansRepository,
    private val cacheSource: ServicePlansDatabaseSource,
    private val clock: Clock
) : ServicePlansProvider {

    override fun getPlansFlow(): Flow<List<Plan>> =
        flow {
            emit(cacheSource.getPlans())

            val freshPlans = freshSource.getPlans()
                .filter { plan -> plan.isAvailable(at = clock.now()) }

            emit(freshPlans)

            cacheSource.insertAll(freshPlans)
        }
}
