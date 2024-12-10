package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a mooncloak VPN server.
 *
 * @property [id] The unique identifier for this server.
 *
 * @property [name] A human-readable name for this server.
 *
 * @property [countryCode] The [CountryCode] representing the country this server resides in.
 *
 * @property [regionCode] The [RegionCode] representing the region this server resides in.
 *
 * @property [status] The current [ServerStatus] details.
 *
 * @property [created] When this server was first created.
 *
 * @property [updated] When this server was last updated.
 *
 * @property [uri] The URI [String] to this server instance.
 *
 * @property [ipAddress] The IP Address for this server instance.
 *
 * @property [self] An optional URI [String] that points to a website describing this server instance.
 *
 * @property [connectionTypes] The [ConnectionType]s that this server supports.
 *
 * @property [vpnProtocols] The [VPNProtocol]s that this server supports.
 */
@Immutable
@Serializable
public data class Server public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "name") public val name: String,
    @SerialName(value = "country_code") public val countryCode: CountryCode,
    @SerialName(value = "region_code") public val regionCode: RegionCode? = null,
    @SerialName(value = "status") public val status: ServerStatus,
    @SerialName(value = "created") public val created: Instant? = null,
    @SerialName(value = "updated") public val updated: Instant? = null,
    @SerialName(value = "uri") public val uri: String,
    @SerialName(value = "ip") public val ipAddress: String? = null,
    @SerialName(value = "self") public val self: String? = null,
    @SerialName(value = "connection_types") public val connectionTypes: List<ConnectionType> = emptyList(),
    @SerialName(value = "vpn_protocols") public val vpnProtocols: List<VPNProtocol> = emptyList()
)
