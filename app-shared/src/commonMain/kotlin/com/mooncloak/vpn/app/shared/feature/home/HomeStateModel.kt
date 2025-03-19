package com.mooncloak.vpn.app.shared.feature.home

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.app.shared.feature.home.model.HomeFeedItem
import com.mooncloak.vpn.api.shared.server.VPNConnectionStatus
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.app.shared.model.NotificationStateModel
import com.mooncloak.vpn.network.core.vpn.VPNConnection

@Immutable
public data class HomeStateModel public constructor(
    public val subscription: ServiceSubscription? = null,
    public val localIpAddress: String? = null,
    public val publicIpAddress: String? = null,
    public val servers: List<Server> = emptyList(),
    public val connection: VPNConnection = VPNConnection.Disconnected(),
    public val items: List<HomeFeedItem> = emptyList(),
    public val moonShieldEnabled: Boolean = false,
    public val isCheckingStatus: Boolean = true,
    public val isLoading: Boolean = false,
    public val errorMessage: NotificationStateModel? = null,
    public val successMessage: NotificationStateModel? = null
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
    inline get() = servers.firstOrNull()?.name

public val HomeStateModel.connectedIpAddress: String?
    inline get() = if (this.isDisconnected) {
        publicIpAddress ?: localIpAddress
    } else {
        servers.firstOrNull()?.let { it.ipV4Address ?: it.ipV6Address }
    }
