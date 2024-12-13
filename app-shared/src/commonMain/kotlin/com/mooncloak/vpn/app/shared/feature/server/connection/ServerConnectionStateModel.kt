package com.mooncloak.vpn.app.shared.feature.server.connection

import androidx.compose.runtime.Immutable

@Immutable
public data class ServerConnectionStateModel public constructor(
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
