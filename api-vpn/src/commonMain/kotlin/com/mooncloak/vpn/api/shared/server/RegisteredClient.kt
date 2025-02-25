package com.mooncloak.vpn.api.shared.server

import com.mooncloak.vpn.api.shared.key.Base64Key
import com.mooncloak.vpn.api.shared.vpn.VPNProtocol
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a registered client for the mooncloak VPN service.
 *
 * @property [id] A unique identifier for this registered client.
 *
 * @property [created] The [Instant] that this registered client model was first created.
 *
 * @property [updated] The [Instant] that this registered client model was last updated.
 *
 * @property [registered] The [Instant] that this client was registered.
 *
 * @property [publicKey] The public key of the client. This is required so that the client can connect with this node.
 *
 * @property [publicKeyId] A unique identifier value used to identify the [publicKey].
 *
 * @property [clientId] A unique identifier value for the client.
 *
 * @property [allowedIpAddresses] An optional [List] of allowed IP Address [String] values. If this value is `null`,
 * then any IP Address is allowed.
 *
 * @property [endpoint] The WireGuard endpoint to connect to the peer node.
 *
 * @property [persistentKeepAlive] The persistent keep alive property set on the WireGuard configuration for this peer.
 *
 * @property [expiration] The [Instant] this peer expires and should be removed from the WireGuard configuration.
 */
@Serializable
public data class RegisteredClient public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "token_id") public val tokenId: String? = null,
    @SerialName(value = "client_id") public val clientId: String? = null,
    @SerialName(value = "server_id") public val serverId: String? = null,
    @SerialName(value = "created") public val created: Instant,
    @SerialName(value = "updated") public val updated: Instant,
    @SerialName(value = "registered") public val registered: Instant,
    @SerialName(value = "expiration") public val expiration: Instant? = null,
    @SerialName(value = "protocol") public val protocol: VPNProtocol,
    @SerialName(value = "public_key") public val publicKey: Base64Key,
    @SerialName(value = "public_key_id") public val publicKeyId: String? = null,
    @SerialName(value = "pre_shared_key") public val preSharedKey: Base64Key? = null,
    @SerialName(value = "assigned_address") public val assignedAddress: String,
    @SerialName(value = "allowed_ips") public val allowedIpAddresses: List<String>? = null,
    @SerialName(value = "persistent_keep_alive") public val persistentKeepAlive: Int? = null,
    @SerialName(value = "endpoint") public val endpoint: String? = null
)
