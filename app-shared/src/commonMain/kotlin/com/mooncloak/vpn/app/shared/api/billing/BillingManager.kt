package com.mooncloak.vpn.app.shared.api.billing

import com.mooncloak.vpn.app.shared.api.plan.Plan

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
