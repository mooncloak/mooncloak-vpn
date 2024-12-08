package com.mooncloak.vpn.app.shared.feature.country

import androidx.compose.runtime.Immutable

@Immutable
public data class CountryListStateModel public constructor(
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
