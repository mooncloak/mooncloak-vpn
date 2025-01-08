package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnection
import com.mooncloak.vpn.app.shared.api.server.ipAddress
import com.mooncloak.vpn.app.shared.api.vpn.defaultTunnel
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
import com.mooncloak.vpn.app.shared.util.time.DateTimeFormatter
import com.mooncloak.vpn.app.shared.util.time.Full
import com.mooncloak.vpn.app.shared.util.time.format
import kotlinx.datetime.Clock
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
                            .animateItem(),
                        since = viewModel.state.current.value.connection.timestamp ?: Clock.System.now()
                    )
                }
            }

            item(key = "ServerLocationItem") {
                ServerLocationCard(
                    modifier = Modifier.fillMaxWidth()
                        .animateItem(),
                    countryName = viewModel.state.current.value.server?.country?.name ?: "",
                    regionName = viewModel.state.current.value.server?.region?.name,
                    serverName = server.name,
                    flagImageUri = viewModel.state.current.value.server?.country?.flag
                )
            }

            if (viewModel.state.current.value.isConnectedServer) {
                item(key = "ServerIpAddressItem") {
                    IpAddressCard(
                        modifier = Modifier.fillMaxWidth()
                            .animateItem(),
                        deviceIpAddress = viewModel.state.current.value.localNetworkInfo?.ipAddress ?: "",
                        serverIpAddress = server.ipAddress ?: "",
                        hideDeviceIpAddress = hideLocalIpAddress.value,
                        onHideDeviceIpAddressChanged = {
                            hideLocalIpAddress.value = !hideLocalIpAddress.value
                        }
                    )
                }

                (viewModel.state.current.value.connection as? VPNConnection.Connected)?.let { connection ->
                    item(key = "ServerSpeedItem") {
                        val tunnel = connection.defaultTunnel

                        SpeedCard(
                            modifier = Modifier.fillMaxWidth()
                                .animateItem(),
                            downloadBits = tunnel?.stats?.rxThroughput,
                            uploadBits = tunnel?.stats?.txThroughput
                        )
                    }

                    item(key = "ServerUsageItem") {
                        val tunnel = connection.defaultTunnel

                        UsageCard(
                            modifier = Modifier.fillMaxWidth()
                                .animateItem(),
                            downloadBytes = tunnel?.stats?.totalRx,
                            uploadBytes = tunnel?.stats?.totalTx
                        )
                    }
                }
            }

            item(key = "ServerInfoItem") {
                ServerInfoCard(
                    modifier = Modifier.fillMaxWidth()
                        .animateItem(),
                    country = viewModel.state.current.value.server?.country?.name,
                    region = viewModel.state.current.value.server?.region?.name,
                    serverName = server.name,
                    connectedLabel = if (viewModel.state.current.value.isConnectedServer) {
                        stringResource(Res.string.server_details_info_field_connected)
                    } else {
                        stringResource(Res.string.server_details_info_field_last_connected)
                    },
                    connected = viewModel.state.current.value.connectionTimestamp?.let { timestamp ->
                        DateTimeFormatter.Full.format(timestamp)
                    },
                    ipAddress = viewModel.state.current.value.server?.ipAddress,
                    serverLoad = null,
                    protocol = viewModel.state.current.value.server?.protocols?.firstOrNull()?.value,
                    performance = null
                )
            }

            if (viewModel.state.current.value.connection is VPNConnection.Connecting) {
                item(key = "ConnectionItem") {
                    LoadingCard(
                        modifier = Modifier.fillMaxWidth()
                            .animateItem()
                    )
                }
            }

            item(key = "ServerConnectActionItem") {
                Button(
                    modifier = Modifier.sizeIn(maxWidth = 400.dp)
                        .fillMaxWidth()
                        .animateItem(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewModel.state.current.value.isConnectedServer) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        contentColor = if (viewModel.state.current.value.isConnectedServer) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        }
                    ),
                    enabled = viewModel.state.current.value.connection !is VPNConnection.Connecting,
                    onClick = {
                        viewModel.toggleConnection()
                    }
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

            item(key = "BottomSpacing") {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
