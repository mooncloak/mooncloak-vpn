package com.mooncloak.vpn.api.shared.server

import androidx.compose.runtime.Immutable
import com.mooncloak.kodetools.locale.LocationCode
import com.mooncloak.vpn.api.shared.vpn.VPNProtocol
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class ServerFilters public constructor(
    @SerialName(value = "location") public val locationCode: LocationCode? = null,
    @SerialName(value = "connection_types") public val connectionTypes: Set<ConnectionType>? = null,
    @SerialName(value = "protocols") public val vpnProtocols: Set<VPNProtocol>? = null
)
