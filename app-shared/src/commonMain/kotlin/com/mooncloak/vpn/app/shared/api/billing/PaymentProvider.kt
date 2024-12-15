package com.mooncloak.vpn.app.shared.api.billing

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
public value class PaymentProvider public constructor(
    public val value: String
) {

    public companion object {

        public val Mooncloak: PaymentProvider = PaymentProvider(value = "mooncloak")
        public val GooglePlay: PaymentProvider = PaymentProvider(value = "google_play")
    }
}
