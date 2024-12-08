package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class ServerFilters public constructor(
    @SerialName(value = "country") public val countryCode: CountryCode? = null,
    @SerialName(value = "region") public val regionCode: RegionCode? = null,
    @SerialName(value = "connection_types") public val connectionTypes: Set<ConnectionType>? = null,
    @SerialName(value = "vpn_protocols") public val vpnProtocols: Set<VPNProtocol>? = null
)
