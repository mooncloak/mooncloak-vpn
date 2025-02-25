package com.mooncloak.vpn.app.shared.feature.home

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.network.LocalNetworkInfo
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.vpn.VPNConnection
import com.mooncloak.vpn.app.shared.feature.home.model.HomeFeedItem
import com.mooncloak.vpn.api.shared.server.VPNConnectionStatus
import com.mooncloak.vpn.api.shared.service.ServiceSubscription

@Immutable
public data class HomeStateModel public constructor(
    public val subscription: ServiceSubscription? = null,
    public val localNetwork: LocalNetworkInfo? = null,
    public val deviceIpAddress: String? = null,
    public val servers: List<Server> = emptyList(),
    public val connection: VPNConnection = VPNConnection.Disconnected(),
    public val items: List<HomeFeedItem> = emptyList(),
    public val isCheckingStatus: Boolean = true,
    public val isLoading: Boolean = false,
    public val errorMessage: String? = null
)

public val HomeStateModel.connectionStatus: VPNConnectionStatus
    inline get() = if (isCheckingStatus) VPNConnectionStatus.Checking else connection.status

public val HomeStateModel.isConnected: Boolean
    inline get() = connectionStatus == VPNConnectionStatus.Connected

public val HomeStateModel.isDisconnected: Boolean
    inline get() = connectionStatus == VPNConnectionStatus.Disconnected

public val HomeStateModel.isConnecting: Boolean
    inline get() = connectionStatus == VPNConnectionStatus.Connecting

public val HomeStateModel.connectedName: String?
    inline get() = if (this.isDisconnected) {
        localNetwork?.country?.name
    } else {
        servers.firstOrNull()?.name
    }

public val HomeStateModel.connectedIpAddress: String?
    inline get() = if (this.isDisconnected) {
        deviceIpAddress ?: localNetwork?.ipAddress
    } else {
        servers.firstOrNull()?.let { it.ipV4Address ?: it.ipV6Address }
    }
