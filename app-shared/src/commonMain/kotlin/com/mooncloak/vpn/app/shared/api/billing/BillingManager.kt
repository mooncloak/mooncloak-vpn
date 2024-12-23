package com.mooncloak.vpn.app.shared.api.billing

import com.mooncloak.vpn.app.shared.api.plan.Plan
import com.mooncloak.vpn.app.shared.api.plan.VPNServicePlansRepository
import kotlin.coroutines.cancellation.CancellationException
import kotlin.jvm.Throws

public interface BillingManager : AutoCloseable,
    VPNServicePlansRepository {

    public val isActive: Boolean

    public fun start()

    public fun cancel()

    public override fun close() {
        cancel()
    }

    @Throws(IllegalStateException::class, CancellationException::class)
    public suspend fun purchasePlan(plan: Plan)

    public companion object
}
