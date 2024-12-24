package com.mooncloak.vpn.app.shared.feature.main

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionStatus
import com.mooncloak.vpn.app.shared.feature.app.MainDestination
import com.mooncloak.vpn.app.shared.feature.main.model.MainDestinationStateModel
import com.mooncloak.vpn.app.shared.feature.main.model.states

@Immutable
public data class MainStateModel public constructor(
    public val startDestination: MainDestination = MainDestination.Home,
    public val destinationStates: Set<MainDestinationStateModel> = MainDestination.states(
        startDestination = startDestination
    ),
    public val serverConnection: ServerConnectionStatus = ServerConnectionStatus.Disconnected
)
