package com.mooncloak.vpn.app.shared.api.server

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.location.Country
import com.mooncloak.vpn.app.shared.api.location.Region
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
 * @property [country] The [Country] representing the country this server resides in.
 *
 * @property [region] The [Region] representing the region this server resides in.
 *
 * @property [status] The current [ServerStatus] details.
 *
 * @property [created] When this server was first created.
 *
 * @property [updated] When this server was last updated.
 *
 * @property [uri] The URI [String] to this server instance.
 *
 * @property [ipV4Address] The IPv4 Address for this server instance.
 *
 * @property [ipV6Address] The IPv6 Address for this server instance.
 *
 * @property [self] An optional URI [String] that points to a website describing this server instance.
 *
 * @property [connectionTypes] The [ConnectionType]s that this server supports.
 *
 * @property [protocols] The [VPNProtocol]s that this server supports.
 *
 * @property [requiresSubscription] Whether connecting to this server requires a subscription or not.
 */
@Immutable
@Serializable
public data class Server public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "name") public val name: String,
    @SerialName(value = "country") public val country: Country? = null,
    @SerialName(value = "region") public val region: Region? = null,
    @SerialName(value = "status") public val status: ServerStatus? = null,
    @SerialName(value = "created") public val created: Instant? = null,
    @SerialName(value = "updated") public val updated: Instant? = null,
    @SerialName(value = "ipv4") public val ipV4Address: String? = null,
    @SerialName(value = "ipv6") public val ipV6Address: String? = null,
    @SerialName(value = "port") public val port: Int? = null,
    @SerialName(value = "uri") public val uri: String? = null,
    @SerialName(value = "self") public val self: String? = null,
    @SerialName(value = "public_key") public val publicKey: String? = null,
    @SerialName(value = "connection_types") public val connectionTypes: List<ConnectionType> = emptyList(),
    @SerialName(value = "vpn_protocols") public val protocols: List<VPNProtocol> = emptyList(),
    @SerialName(value = "tags") public val tags: List<String> = emptyList(),
    @SerialName(value = "subscription") public val requiresSubscription: Boolean = true
)

public val Server.ipAddress: String?
    inline get() = ipV4Address ?: ipV6Address

@Suppress("NOTHING_TO_INLINE")
public inline fun Server.requireIpAddress(): String =
    this.ipAddress ?: error("Required IP Address for Server '${this.id}' was missing.")

/**
 * Determines whether the current user can connection to this VPN [Server].
 *
 * @param [hasSubscription] Whether the user has a valid subscription or not.
 *
 * @return `true` if a connection to the VPN server can be made, `false` otherwise.
 */
public fun Server.isConnectable(hasSubscription: Boolean): Boolean =
    (ipAddress != null && publicKey != null) && (!requiresSubscription || hasSubscription)
