package com.mooncloak.vpn.api.shared.plan

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class BillingProvider public constructor(
    public val value: String
) {

    public companion object {

        public val Mooncloak: BillingProvider = BillingProvider(value = "mooncloak")
        public val GooglePlay: BillingProvider = BillingProvider(value = "google_play")
    }
}
