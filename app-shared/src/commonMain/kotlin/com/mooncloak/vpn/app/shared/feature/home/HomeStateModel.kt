package com.mooncloak.vpn.app.shared.feature.home

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.LocalNetworkInfo
import com.mooncloak.vpn.app.shared.api.Server
import com.mooncloak.vpn.app.shared.feature.home.model.HomeFeedItem
import com.mooncloak.vpn.app.shared.api.ServerConnectionStatus
import com.mooncloak.vpn.app.shared.api.ServiceSubscription

@Immutable
public data class HomeStateModel public constructor(
    public val subscription: ServiceSubscription? = null,
    public val localNetwork: LocalNetworkInfo? = null,
    public val servers: List<Server> = emptyList(),
    public val connection: ServerConnectionStatus = ServerConnectionStatus.Disconnected,
    public val items: List<HomeFeedItem> = emptyList(),
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)

public val HomeStateModel.isConnected: Boolean
    inline get() = connection == ServerConnectionStatus.Connected

public val HomeStateModel.isDisconnected: Boolean
    inline get() = connection == ServerConnectionStatus.Disconnected

public val HomeStateModel.isConnecting: Boolean
    inline get() = connection == ServerConnectionStatus.Connecting

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
        servers.firstOrNull()?.ipAddress
    }
