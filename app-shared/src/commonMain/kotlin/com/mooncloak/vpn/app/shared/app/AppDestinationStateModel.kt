package com.mooncloak.vpn.app.shared.app

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
public data class AppDestinationStateModel public constructor(
    public val destination: AppDestination,
    public val isBadged: Boolean = false,
    public val isSelected: Boolean = false,
    public val isVisible: Boolean = true,
    public val isEnabled: Boolean = true
)
