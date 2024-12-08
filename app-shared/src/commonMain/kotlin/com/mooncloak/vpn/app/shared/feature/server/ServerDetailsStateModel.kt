package com.mooncloak.vpn.app.shared.feature.server

import androidx.compose.runtime.Immutable

@Immutable
public data class ServerDetailsStateModel public constructor(
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
