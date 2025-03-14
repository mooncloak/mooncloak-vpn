package com.mooncloak.vpn.app.shared.feature.server.connection

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.network.core.vpn.VPNConnection

@Immutable
public data class ServerConnectionStateModel public constructor(
    public val connection: VPNConnection = VPNConnection.Disconnected(),
    public val server: Server? = null,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)
