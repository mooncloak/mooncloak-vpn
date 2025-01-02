package com.mooncloak.vpn.app.shared.feature.main.model

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.feature.app.MainDestination
import kotlinx.serialization.Serializable

@Serializable
@Immutable
public data class MainDestinationStateModel public constructor(
    public val destination: MainDestination,
    public val badged: Boolean = false,
    public val selected: Boolean = false,
    public val visible: Boolean = true,
    public val enabled: Boolean = true
)

public fun MainDestination.Companion.states(startDestination: MainDestination): Set<MainDestinationStateModel> =
    setOf(
        MainDestinationStateModel(
            destination = MainDestination.Home,
            selected = startDestination is MainDestination.Home
        ),
        MainDestinationStateModel(
            destination = MainDestination.Servers,
            selected = startDestination is MainDestination.Servers
        ),
        MainDestinationStateModel(
            destination = MainDestination.Support,
            selected = startDestination is MainDestination.Support
        ),
        MainDestinationStateModel(
            destination = MainDestination.Settings,
            selected = startDestination is MainDestination.Settings
        )
    )
