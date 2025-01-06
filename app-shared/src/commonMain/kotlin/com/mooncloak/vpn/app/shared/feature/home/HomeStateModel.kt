package com.mooncloak.vpn.app.shared.feature.home

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkInfo
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnection
import com.mooncloak.vpn.app.shared.feature.home.model.HomeFeedItem
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionStatus
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscription

@Immutable
public data class HomeStateModel public constructor(
    public val subscription: ServiceSubscription? = null,
    public val localNetwork: LocalNetworkInfo? = null,
    public val servers: List<Server> = emptyList(),
    public val connection: ServerConnection = ServerConnection.Disconnected(),
    public val items: List<HomeFeedItem> = emptyList(),
    public val isCheckingStatus: Boolean = true,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)

public val HomeStateModel.connectionStatus: ServerConnectionStatus
    inline get() = if (isCheckingStatus) ServerConnectionStatus.Checking else connection.status

public val HomeStateModel.isConnected: Boolean
    inline get() = connectionStatus == ServerConnectionStatus.Connected

public val HomeStateModel.isDisconnected: Boolean
    inline get() = connectionStatus == ServerConnectionStatus.Disconnected

public val HomeStateModel.isConnecting: Boolean
    inline get() = connectionStatus == ServerConnectionStatus.Connecting

public val HomeStateModel.connectedName: String?
    inline get() = if (this.isDisconnected) {
        localNetwork?.country?.name
    } else {
        servers.firstOrNull()?.name
    }

public val HomeStateModel.connectedIpAddress: String?
    inline get() = if (this.isDisconnected) {
        localNetwork?.ipAddress
    } else {
        servers.firstOrNull()?.let { it.ipV4Address ?: it.ipV6Address }
    }
