package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.location.Country
import com.mooncloak.vpn.app.shared.api.location.Region
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkInfo
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnection
import kotlinx.datetime.Instant

@Immutable
public data class ServerDetailsStateModel public constructor(
    public val server: Server? = null,
    public val country: Country? = null,
    public val region: Region? = null,
    public val connection: VPNConnection = VPNConnection.Disconnected(),
    public val lastConnected: Instant? = null,
    public val localNetworkInfo: LocalNetworkInfo? = null,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)

public val ServerDetailsStateModel.isConnectedServer: Boolean
    inline get() = connection is VPNConnection.Connected && server == connection.server

public val ServerDetailsStateModel.connectionTimestamp: Instant?
    inline get() = if (this.isConnectedServer) {
        (connection as? VPNConnection.Connected)?.timestamp ?: lastConnected
    } else {
        lastConnected
    }
