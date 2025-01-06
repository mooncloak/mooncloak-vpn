package com.mooncloak.vpn.app.shared.feature.server.list

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnection
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscription

@Immutable
public data class ServerListStateModel public constructor(
    public val servers: List<Server> = emptyList(),
    public val connection: ServerConnection = ServerConnection.Disconnected(),
    public val subscription: ServiceSubscription? = null,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
