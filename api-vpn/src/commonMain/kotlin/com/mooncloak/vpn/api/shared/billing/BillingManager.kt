package com.mooncloak.vpn.api.shared.billing

import com.mooncloak.vpn.api.shared.plan.Plan

public interface BillingManager : AutoCloseable {

    public val isActive: Boolean

    public fun start()

    public fun cancel()

    public override fun close() {
        cancel()
    }

    public suspend fun purchase(plan: Plan): BillingResult

    public companion object
}
