package com.mooncloak.vpn.app.shared.feature.server.list

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.network.core.vpn.VPNConnection

@Immutable
public data class ServerListStateModel public constructor(
    public val servers: List<Server> = emptyList(),
    public val connection: VPNConnection = VPNConnection.Disconnected(),
    public val subscription: ServiceSubscription? = null,
    public val isPreRelease: Boolean = false,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)

@Suppress("NOTHING_TO_INLINE")
public inline fun ServerListStateModel.hasSubscription(): Boolean =
    this.subscription != null
