package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents status information about a VPN [Server].
 *
 * @property [active] Whether this server is actively running.
 *
 * @property [connectable] Whether this server is capable to be connected to.
 *
 * @property [healthy] Whether this server is generally considered healthy.
 *
 * @property [connectionCount] The amount of current connections to this server, or `null` if unknown.
 *
 * @property [load] The current server load for this server relative to the maximum, or `null` if unknown. If provided,
 * this is a value between 0f and 1f, where 0f means no connections, and 1f means the maximum amount of connections is
 * reached.
 */
@Immutable
@Serializable
public data class ServerStatus public constructor(
    @SerialName(value = "active") public val active: Boolean = true,
    @SerialName(value = "connectable") public val connectable: Boolean = true,
    @SerialName(value = "healthy") public val healthy: Boolean = true,
    @SerialName(value = "connections") public val connectionCount: Int? = null,
    @SerialName(value = "load") public val load: Float? = null
)
