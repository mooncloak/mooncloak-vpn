package com.mooncloak.vpn.app.shared.feature.server.list

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.server.Server

@Immutable
public data class ServerListStateModel public constructor(
    public val servers: List<Server> = emptyList(),
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
