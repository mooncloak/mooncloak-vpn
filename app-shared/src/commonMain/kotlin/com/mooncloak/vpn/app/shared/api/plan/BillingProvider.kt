package com.mooncloak.vpn.app.shared.api.plan

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
