package com.mooncloak.vpn.api.shared.billing

import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.api.shared.token.TransactionToken

public interface BillingManager : AutoCloseable {

    public val isActive: Boolean

    public fun start()

    public fun cancel()

    public override fun close() {
        cancel()
    }

    public suspend fun purchase(plan: Plan): BillingResult

    public fun handleReceipt(
        token: TransactionToken,
        state: String? = null, // Similar to OAuth's state parameter.
    )

    public companion object
}
