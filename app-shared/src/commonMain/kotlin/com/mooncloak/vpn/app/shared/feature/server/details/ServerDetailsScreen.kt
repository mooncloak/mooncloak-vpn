package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnection
import com.mooncloak.vpn.app.shared.api.server.ipAddress
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.server.details.composable.CloakedLayout
import com.mooncloak.vpn.app.shared.feature.server.details.composable.LoadingCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.IpAddressCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.ServerInfoCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.ServerLocationCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.SpeedCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.UsageCard
import com.mooncloak.vpn.app.shared.feature.server.details.di.createServerDetailsComponent
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.server_details_action_connect
import com.mooncloak.vpn.app.shared.resource.server_details_action_disconnect
import com.mooncloak.vpn.app.shared.resource.server_details_info_field_connected
import com.mooncloak.vpn.app.shared.resource.server_details_info_field_last_connected
import org.jetbrains.compose.resources.stringResource

@Composable
public fun ServerDetailsScreen(
    server: Server,
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createServerDetailsComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val lazyListState = rememberLazyListState()
    val hideLocalIpAddress = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.load(server)
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (viewModel.state.current.value.isConnectedServer) {
                item(key = "ServerCloakedItem") {
                    CloakedLayout(
                        modifier = Modifier.fillMaxWidth()
                            .aspectRatio(1f)
                    )
                }
            }

            item(key = "ServerLocationItem") {
                ServerLocationCard(
                    modifier = Modifier.fillMaxWidth(),
                    countryName = viewModel.state.current.value.country?.name ?: "",
                    regionName = viewModel.state.current.value.region?.name,
                    serverName = server.name,
                    flagImageUri = viewModel.state.current.value.country?.flag
                )
            }

            if (viewModel.state.current.value.isConnectedServer) {
                item(key = "ServerIpAddressItem") {
                    IpAddressCard(
                        modifier = Modifier.fillMaxWidth(),
                        deviceIpAddress = viewModel.state.current.value.localNetworkInfo?.ipAddress ?: "",
                        serverIpAddress = server.ipAddress ?: "",
                        hideDeviceIpAddress = hideLocalIpAddress.value,
                        onHideDeviceIpAddressChanged = {
                            hideLocalIpAddress.value = !hideLocalIpAddress.value
                        }
                    )
                }

                (viewModel.state.current.value.connection as? ServerConnection.Connected)?.let { connection ->
                    item(key = "ServerSpeedItem") {
                        SpeedCard(
                            modifier = Modifier.fillMaxWidth(),
                            downloadBits = connection.rxThroughput,
                            uploadBits = connection.txThroughput
                        )
                    }

                    item(key = "ServerUsageItem") {
                        UsageCard(
                            modifier = Modifier.fillMaxWidth(),
                            downloadBytes = connection.totalRx,
                            uploadBytes = connection.totalTx
                        )
                    }
                }
            }

            item(key = "ServerInfoItem") {
                ServerInfoCard(
                    modifier = Modifier.fillMaxWidth(),
                    country = viewModel.state.current.value.country?.name ?: "",
                    region = viewModel.state.current.value.region?.name,
                    serverName = server.name,
                    connectedLabel = if (viewModel.state.current.value.isConnectedServer) {
                        stringResource(Res.string.server_details_info_field_connected)
                    } else {
                        stringResource(Res.string.server_details_info_field_last_connected)
                    },
                    connected = null, // TODO: Format date time
                    ipAddress = viewModel.state.current.value.server?.ipAddress,
                    serverLoad = null,
                    protocol = viewModel.state.current.value.server?.protocols?.firstOrNull()?.value,
                    performance = null
                )
            }

            if (viewModel.state.current.value.connection is ServerConnection.Connecting) {
                item(key = "ConnectionItem") {
                    LoadingCard(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            item(key = "ServerConnectActionItem") {
                Button(
                    modifier = Modifier.sizeIn(maxWidth = 400.dp)
                        .fillMaxWidth(),
                    onClick = {
                        // TODO:
                    },
                    enabled = viewModel.state.current.value.connection !is ServerConnection.Connecting
                ) {
                    Text(
                        text = if (viewModel.state.current.value.isConnectedServer) {
                            stringResource(Res.string.server_details_action_disconnect)
                        } else {
                            stringResource(Res.string.server_details_action_connect)
                        }
                    )
                }
            }
        }
    }
}
