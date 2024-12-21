package com.mooncloak.vpn.app.shared.feature.collaborator.list

import androidx.compose.runtime.Immutable

@Immutable
public data class CollaboratorListStateModel public constructor(
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
