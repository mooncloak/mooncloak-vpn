package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkInfo
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnection

@Immutable
public data class ServerDetailsStateModel public constructor(
    public val server: Server? = null,
    public val connection: ServerConnection = ServerConnection.Disconnected,
    public val localNetworkInfo: LocalNetworkInfo? = null,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
