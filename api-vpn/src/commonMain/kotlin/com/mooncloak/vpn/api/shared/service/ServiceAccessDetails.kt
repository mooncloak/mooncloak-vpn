package com.mooncloak.vpn.api.shared.service

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class ServiceAccessDetails public constructor(
    @SerialName(value = "tokens") public val tokens: ServiceTokens,
    @SerialName(value = "subscription") public val subscription: ServiceSubscription
)
