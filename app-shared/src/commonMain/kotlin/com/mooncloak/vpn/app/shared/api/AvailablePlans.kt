package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents details about the available [Plan]s for the mooncloak VPN service.
 *
 * @property [plans] The [List] of available [Plan]s. Note that not all [Plan]s will be active. Check each
 * [Plan.active] property to determine if it is active.
 */
@Immutable
@Serializable
public data class AvailablePlans public constructor(
    @SerialName(value = "plans") public val plans: List<Plan> = emptyList()
)
