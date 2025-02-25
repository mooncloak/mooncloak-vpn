package com.mooncloak.vpn.app.shared.feature.server.region

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.server.Server

@Immutable
public data class RegionServerListStateModel public constructor(
    public val servers: List<Server> = emptyList(),
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
