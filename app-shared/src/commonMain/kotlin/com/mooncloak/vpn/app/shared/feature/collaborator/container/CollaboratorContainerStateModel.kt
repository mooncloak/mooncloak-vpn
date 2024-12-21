package com.mooncloak.vpn.app.shared.feature.collaborator.container

import androidx.compose.runtime.Immutable

@Immutable
public data class CollaboratorContainerStateModel public constructor(
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
