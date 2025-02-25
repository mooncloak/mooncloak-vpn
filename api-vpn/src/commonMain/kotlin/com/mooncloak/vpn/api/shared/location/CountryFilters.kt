package com.mooncloak.vpn.api.shared.location

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.server.ConnectionType
import com.mooncloak.vpn.api.shared.vpn.VPNProtocol
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class CountryFilters public constructor(
    @SerialName(value = "connection_types") public val connectionTypes: Set<ConnectionType>? = null,
    @SerialName(value = "vpn_protocols") public val vpnProtocols: Set<VPNProtocol>? = null
)
