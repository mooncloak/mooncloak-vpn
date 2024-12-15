package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a country that contains a mooncloak VPN server.
 *
 * @property [code] The [CountryCode] of the country.
 *
 * @property [regions] The [Region]s within this country that contain a mooncloak VPN server.
 *
 * @property [name] The name of the country.
 *
 * @property [flag] The image URI of the country's flag.
 *
 * @property [connectionDescription] Details about the connection to mooncloak VPN servers within this country.
 *
 * @property [connectionTypes] The list of [ConnectionType]s supported by this country. This is a derived value.
 *
 * @property [serverCount] The amount of servers available within this country.
 */
@Immutable
@Serializable
public data class Country public constructor(
    @SerialName(value = "code") public val code: CountryCode,
    @SerialName(value = "name") public val name: String,
    @SerialName(value = "flag") public val flag: String? = null,
    @SerialName(value = "regions") public val regions: List<Region> = emptyList(),
    @SerialName(value = "region_type") public val regionType: String? = null,
    @SerialName(value = "description") public val connectionDescription: String? = null,
    @SerialName(value = "connection_types") public val connectionTypes: List<ConnectionType> = emptyList(),
    @SerialName(value = "server_count") public val serverCount: Int? = null
)
