package com.mooncloak.vpn.app.shared.api.server

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ServerConnectionRecord public constructor(
    @SerialName(value = "server") public val server: Server,
    @SerialName(value = "connected") public val lastConnected: Instant? = null,
    @SerialName(value = "starred") public val starred: Instant? = null,
    @SerialName(value = "note") public val note: String? = null
)
