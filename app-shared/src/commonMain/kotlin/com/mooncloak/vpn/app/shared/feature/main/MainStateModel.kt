package com.mooncloak.vpn.app.shared.feature.main

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnection
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscription
import com.mooncloak.vpn.app.shared.feature.app.MainDestination
import com.mooncloak.vpn.app.shared.feature.main.model.MainDestinationStateModel
import com.mooncloak.vpn.app.shared.feature.main.model.states

@Immutable
public data class MainStateModel public constructor(
    public val startDestination: MainDestination = MainDestination.Home,
    public val destinationStates: Set<MainDestinationStateModel> = MainDestination.states(
        startDestination = startDestination
    ),
    public val connection: VPNConnection = VPNConnection.Disconnected(),
    public val subscription: ServiceSubscription? = null,
    public val defaultServer: Server? = null,
    public val errorMessage: String? = null
)
