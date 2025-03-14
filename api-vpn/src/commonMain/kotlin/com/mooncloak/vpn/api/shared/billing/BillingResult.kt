package com.mooncloak.vpn.api.shared.billing

import com.mooncloak.vpn.api.shared.plan.Plan
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Represents the result of the [BillingManager.purchase] operation.
 */
public sealed interface BillingResult {

    public val code: Int?

    public val message: String?

    public val plans: List<Plan>

    /**
     * Represents a successful [BillingResult] state.
     */
    public data class Success public constructor(
        override val code: Int? = null,
        override val message: String? = null,
        override val plans: List<Plan> = emptyList()
    ) : BillingResult

    /**
     * Represents a failed [BillingResult] state.
     */
    public data class Failure public constructor(
        override val code: Int? = null,
        override val message: String? = null,
        override val plans: List<Plan> = emptyList(),
        public val cause: Throwable? = null
    ) : BillingResult

    /**
     * Represents a user cancelled [BillingResult] state.
     */
    public data class Cancelled public constructor(
        override val code: Int? = null,
        override val message: String? = null,
        override val plans: List<Plan> = emptyList()
    ) : BillingResult

    public companion object
}

@OptIn(ExperimentalContracts::class)
public inline fun BillingResult.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is BillingResult.Success)
    }

    return this is BillingResult.Success
}

@OptIn(ExperimentalContracts::class)
public inline fun BillingResult.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is BillingResult.Failure)
    }

    return this is BillingResult.Failure
}

@OptIn(ExperimentalContracts::class)
public inline fun BillingResult.isCancelled(): Boolean {
    contract {
        returns(true) implies (this@isCancelled is BillingResult.Cancelled)
    }

    return this is BillingResult.Cancelled
}
