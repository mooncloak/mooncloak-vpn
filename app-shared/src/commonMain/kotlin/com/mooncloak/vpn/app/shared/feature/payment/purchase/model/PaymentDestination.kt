package com.mooncloak.vpn.app.shared.feature.payment.purchase.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public sealed interface PaymentDestination {

    @Immutable
    @Serializable
    @SerialName(value = "landing")
    public data object Plans : PaymentDestination

    @Immutable
    @Serializable
    @SerialName(value = "tutorial")
    public data object Invoice : PaymentDestination
}
