package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class CountryFilters public constructor(
    @SerialName(value = "connection_types") public val connectionTypes: Set<ConnectionType>? = null,
    @SerialName(value = "vpn_protocols") public val vpnProtocols: Set<VPNProtocol>? = null
)
