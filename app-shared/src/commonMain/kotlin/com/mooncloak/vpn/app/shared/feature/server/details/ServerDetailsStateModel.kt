package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.network.core.vpn.VPNConnection
import com.mooncloak.vpn.network.core.vpn.connectedTo
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Immutable
public data class ServerDetailsStateModel public constructor(
    public val server: Server? = null,
    public val connection: VPNConnection = VPNConnection.Disconnected(),
    public val startConnectionDuration: Duration = 0.seconds,
    public val lastConnected: Instant? = null,
    public val deviceIpAddress: String? = null,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)

public val ServerDetailsStateModel.isConnectedServer: Boolean
    inline get() = server != null && connection.connectedTo(server)

public val ServerDetailsStateModel.connectionTimestamp: Instant?
    inline get() = if (this.isConnectedServer) {
        connection.timestamp ?: lastConnected
    } else {
        lastConnected
    }
