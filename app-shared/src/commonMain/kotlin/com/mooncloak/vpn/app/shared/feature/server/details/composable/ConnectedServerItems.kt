package com.mooncloak.vpn.app.shared.feature.server.details.composable

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.api.location.Country
import com.mooncloak.vpn.app.shared.api.location.Region
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkInfo
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnection
import com.mooncloak.vpn.app.shared.api.server.ipAddress

@Suppress("FunctionName")
internal fun LazyListScope.ConnectedServerItems(
    server: Server,
    country: Country,
    region: Region?,
    connection: ServerConnection.Connected,
    localNetwork: LocalNetworkInfo,
    hideLocalIpAddress: Boolean,
    onHideLocalIpAddressChanged: () -> Unit
) {
    item {
        CloakedLayout(
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(1f)
        )
    }

    item {
        ServerLocationCard(
            modifier = Modifier.fillMaxWidth(),
            countryName = country.name,
            regionName = region?.name,
            serverName = server.name,
            flagImageUri = country.flag
        )
    }

    item {
        IpAddressCard(
            modifier = Modifier.fillMaxWidth(),
            deviceIpAddress = localNetwork.ipAddress ?: "",
            serverIpAddress = server.ipAddress ?: "",
            hideDeviceIpAddress = hideLocalIpAddress,
            onHideDeviceIpAddressChanged = onHideLocalIpAddressChanged
        )
    }

    item {
        SpeedCard(
            modifier = Modifier.fillMaxWidth(),
            downloadBits = 10000,
            uploadBits = 1000
        )
    }

    item {
        UsageCard(
            modifier = Modifier.fillMaxWidth(),
            downloadBytes = 10000,
            uploadBytes = 1000
        )
    }

    item {
        ServerInfoCard(
            modifier = Modifier.fillMaxWidth(),
            country = country.name,
            region = region?.name,
            serverName = server.name
        )
    }
}
