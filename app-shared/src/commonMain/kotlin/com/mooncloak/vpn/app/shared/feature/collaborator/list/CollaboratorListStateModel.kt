package com.mooncloak.vpn.app.shared.feature.collaborator.list

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.app.Contributor

@Immutable
public data class CollaboratorListStateModel public constructor(
    public val collaborators: List<Contributor> = emptyList(),
    public val isLoading: Boolean = false,
    public val isError: Boolean = false,
    public val errorMessage: String? = null
)
