package com.mooncloak.vpn.api.shared.location

import com.mooncloak.kodetools.locale.Country
import com.mooncloak.kodetools.locale.Region
import com.mooncloak.vpn.api.shared.server.ConnectionType
import com.mooncloak.vpn.api.shared.vpn.VPNProtocol
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents details about a particular [Country] for the VPN service.
 *
 * @property [country] The [Country] that this model represents details about.
 *
 * @property [connectionDescription] Details about the connection to mooncloak VPN servers within this country.
 *
 * @property [connectionTypes] The list of [ConnectionType]s supported by this country. This is a derived value.
 *
 * @property [protocols] The list of [VPNProtocol]s supported by this country.
 *
 * @property [serverCount] The amount of servers available within this country.
 *
 * @property [regions] The list of [RegionDetails]s representing [Region]s available in this country.
 */
@Serializable
public data class CountryDetails public constructor(
    @SerialName(value = "country") public val country: Country,
    @SerialName(value = "connection") public val connectionDescription: String? = null,
    @SerialName(value = "types") public val connectionTypes: List<ConnectionType> = emptyList(),
    @SerialName(value = "protocols") public val protocols: List<VPNProtocol> = emptyList(),
    @SerialName(value = "server_count") public val serverCount: Int? = null,
    @SerialName(value = "regions") public val regions: List<RegionDetails> = emptyList()
)
