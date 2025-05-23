package com.mooncloak.vpn.app.shared.feature.main

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.app.shared.feature.main.model.MainDestinationStateModel
import com.mooncloak.vpn.app.shared.feature.main.model.states
import com.mooncloak.vpn.network.core.vpn.VPNConnection

@Immutable
public data class MainStateModel public constructor(
    public val destinationStates: Set<MainDestinationStateModel> = MainDestination.states(),
    public val connection: VPNConnection = VPNConnection.Disconnected(),
    public val subscription: ServiceSubscription? = null,
    public val defaultServer: Server? = null,
    public val errorMessage: String? = null
)
