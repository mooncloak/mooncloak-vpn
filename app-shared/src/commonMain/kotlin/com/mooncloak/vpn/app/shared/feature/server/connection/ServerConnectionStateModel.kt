package com.mooncloak.vpn.app.shared.feature.server.connection

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.server.ServerConnection

@Immutable
public data class ServerConnectionStateModel public constructor(
    public val connection: ServerConnection = ServerConnection.Disconnected,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
