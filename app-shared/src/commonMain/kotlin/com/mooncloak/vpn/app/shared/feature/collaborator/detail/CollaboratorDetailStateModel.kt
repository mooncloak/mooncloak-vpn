package com.mooncloak.vpn.app.shared.feature.collaborator.detail

import androidx.compose.runtime.Immutable

@Immutable
public data class CollaboratorDetailStateModel public constructor(
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
