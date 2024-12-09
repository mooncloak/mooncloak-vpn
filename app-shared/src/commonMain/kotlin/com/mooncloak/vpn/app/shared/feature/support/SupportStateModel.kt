package com.mooncloak.vpn.app.shared.feature.support

import androidx.compose.runtime.Immutable

@Immutable
public data class SupportStateModel public constructor(
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
