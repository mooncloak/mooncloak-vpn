package com.mooncloak.vpn.app.shared.api.location

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.server.ConnectionType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public sealed interface Country {

    public val code: CountryCode
    public val name: String
    public val flag: String?
    public val regions: List<Region>
    public val regionType: String?
}

@Immutable
@Serializable
@SerialName(value = "default")
public data class DefaultCountry public constructor(
    @SerialName(value = "code") public override val code: CountryCode,
    @SerialName(value = "name") public override val name: String,
    @SerialName(value = "flag") public override val flag: String? = null,
    @SerialName(value = "regions") public override val regions: List<Region> = emptyList(),
    @SerialName(value = "region_type") public override val regionType: String? = null
) : Country

/**
 * Represents a country that contains a mooncloak VPN server.
 *
 * @property [code] The [CountryCode] of the country.
 *
 * @property [regions] The [RegionWithVPNService]s within this country that contain a mooncloak VPN server.
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
@SerialName(value = "serviceable")
public data class CountryWithVPNService public constructor(
    @SerialName(value = "code") public override val code: CountryCode,
    @SerialName(value = "name") public override val name: String,
    @SerialName(value = "flag") public override val flag: String? = null,
    @SerialName(value = "regions") public override val regions: List<RegionWithVPNService> = emptyList(),
    @SerialName(value = "region_type") public override val regionType: String? = null,
    @SerialName(value = "description") public val connectionDescription: String? = null,
    @SerialName(value = "connection_types") public val connectionTypes: List<ConnectionType> = emptyList(),
    @SerialName(value = "server_count") public val serverCount: Int? = null
) : Country

public operator fun Country.Companion.invoke(
    code: CountryCode,
    name: String,
    flag: String? = null,
    regions: List<Region> = emptyList()
): Country = DefaultCountry(
    code = code,
    name = name,
    flag = flag,
    regions = regions
)

public fun Country.toServiceable(
    connectionDescription: String? = null,
    connectionTypes: List<ConnectionType> = emptyList(),
    serverCount: Int? = null
): CountryWithVPNService = if (this is CountryWithVPNService) {
    this.copy(
        connectionDescription = connectionDescription,
        connectionTypes = connectionTypes,
        serverCount = serverCount
    )
} else {
    CountryWithVPNService(
        code = code,
        name = name,
        flag = flag,
        regions = regions.map { it.toServiceable() },
        connectionDescription = connectionDescription,
        connectionTypes = connectionTypes,
        serverCount = serverCount
    )
}
