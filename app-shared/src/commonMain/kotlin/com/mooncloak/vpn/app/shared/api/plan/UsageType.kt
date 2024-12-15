package com.mooncloak.vpn.app.shared.api.plan

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * Based off of the Stripe API "usage_type" enum value. Configures how the quantity per period should be determined.
 */
@Immutable
@Serializable
@JvmInline
public value class UsageType public constructor(
    public val value: String
) {

    public companion object {

        /**
         * Automatically bills the quantity set when adding it to a subscription.
         */
        public val Licensed: UsageType = UsageType(value = "licensed")

        /**
         * Aggregates the total usage based on usage records.
         */
        public val Metered: UsageType = UsageType(value = "metered")
    }
}
