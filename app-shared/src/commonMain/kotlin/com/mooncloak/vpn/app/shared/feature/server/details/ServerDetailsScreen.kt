package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.vpn.VPNConnection
import com.mooncloak.vpn.api.shared.server.ipAddress
import com.mooncloak.vpn.api.shared.vpn.defaultTunnel
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.composable.CloakedLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.feature.server.details.composable.LoadingCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.IpAddressCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.ServerInfoCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.ServerLocationCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.UsageCard
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_not_available
import com.mooncloak.vpn.app.shared.resource.server_details_action_connect
import com.mooncloak.vpn.app.shared.resource.server_details_action_disconnect
import com.mooncloak.vpn.app.shared.resource.server_details_info_field_connected
import com.mooncloak.vpn.app.shared.resource.server_details_info_field_last_connected
import com.mooncloak.vpn.util.shared.time.DateTimeFormatter
import com.mooncloak.vpn.util.shared.time.Full
import com.mooncloak.vpn.util.shared.time.format
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.milliseconds

@Composable
public fun ServerDetailsScreen(
    state: ServerDetailsBottomSheetState,
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
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.server.value) {
        viewModel.load(state.server.value)
    }

    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = state.bottomSheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            loadingState = derivedStateOf { viewModel.state.current.value.isLoading }
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
                        serverName = state.server.value?.name ?: stringResource(Res.string.global_not_available),
                        flagImageUri = viewModel.state.current.value.server?.country?.flag
                    )
                }

                if (viewModel.state.current.value.isConnectedServer) {
                    item(key = "ServerIpAddressItem") {
                        IpAddressCard(
                            modifier = Modifier.fillMaxWidth()
                                .animateItem(),
                            deviceIpAddress = viewModel.state.current.value.deviceIpAddress ?: "",
                            serverIpAddress = state.server.value?.ipAddress ?: "",
                            hideDeviceIpAddress = hideLocalIpAddress.value,
                            onHideDeviceIpAddressChanged = {
                                hideLocalIpAddress.value = !hideLocalIpAddress.value
                            }
                        )
                    }

                    (viewModel.state.current.value.connection as? VPNConnection.Connected)?.let { connection ->
                        /* TODO: Re-enable speed card when we fix the retrieval of the correct speed values.
                        item(key = "ServerSpeedItem") {
                            val tunnel = connection.defaultTunnel

                            SpeedCard(
                                modifier = Modifier.fillMaxWidth()
                                    .animateItem(),
                                downloadBits = tunnel?.stats?.value?.rxThroughput,
                                uploadBits = tunnel?.stats?.value?.txThroughput
                            )
                        }*/

                        item(key = "ServerUsageItem") {
                            val tunnel = connection.defaultTunnel

                            UsageCard(
                                modifier = Modifier.fillMaxWidth()
                                    .animateItem(),
                                downloadBytes = tunnel?.stats?.value?.totalRx,
                                uploadBytes = tunnel?.stats?.value?.totalTx
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
                        serverName = state.server.value?.name ?: stringResource(Res.string.global_not_available),
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
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
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

                                coroutineScope.launch {
                                    // FIXME: Hacky scroll to top solution
                                    // For some reason, when the state changes to connected, the top item in the lazy list is
                                    // not displayed. Instead, it always shows the second item. You have to scroll to the first
                                    // item. This causes issues with the scrolling of the bottom sheet layout. So we attempt to
                                    // force the scroll to the top so it shows the first item correctly. The underlying issue
                                    // should be resolved and this hacky solution should be removed in the future.
                                    delay(300.milliseconds)
                                    lazyListState.animateScrollToItem(0)
                                }
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
                }

                item(key = "BottomSpacing") {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
