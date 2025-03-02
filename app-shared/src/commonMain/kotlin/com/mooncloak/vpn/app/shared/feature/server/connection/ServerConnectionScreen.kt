package com.mooncloak.vpn.app.shared.feature.server.connection

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.vpn.VPNConnection
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.server.connection.composable.ServerConnectedLayout
import com.mooncloak.vpn.app.shared.feature.server.connection.composable.ServerConnectingLayout
import com.mooncloak.vpn.app.shared.feature.server.connection.composable.ServerConnectionErrorLayout
import com.mooncloak.vpn.app.shared.feature.server.connection.composable.ServerDisconnectedLayout
import com.mooncloak.vpn.app.shared.feature.server.connection.composable.ServerDisconnectingLayout

@Composable
public fun ServerConnectionScreen(
    state: ServerConnectionBottomSheetState,
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createServerConnectionComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }

    LaunchedEffect(state.server.value) {
        state.server.value?.let { server ->
            viewModel.load(server = server)
        }
    }

    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = state.bottomSheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            AnimatedContent(
                targetState = viewModel.state.current.value.connection
            ) { connection ->
                when (connection) {
                    is VPNConnection.Connecting -> ServerConnectingLayout(
                        modifier = Modifier.fillMaxWidth(),
                        server = connection.server
                    )

                    is VPNConnection.Disconnecting -> ServerDisconnectingLayout(
                        modifier = Modifier.fillMaxWidth()
                    )

                    is VPNConnection.Connected -> {
                        val tunnel = connection.tunnels.firstOrNull { it.server != null }
                        val server = tunnel?.server

                        if (tunnel != null && server != null) {
                            ServerConnectedLayout(
                                modifier = Modifier.fillMaxWidth(),
                                server = server,
                                timestamp = connection.timestamp,
                                rxThroughput = tunnel.stats.value?.rxThroughput,
                                txThroughput = tunnel.stats.value?.txThroughput,
                                onDisconnect = {
                                    viewModel.disconnect()
                                }
                            )
                        }
                    }

                    is VPNConnection.Disconnected -> if (connection.errorMessage != null) {
                        ServerConnectionErrorLayout(
                            modifier = Modifier.fillMaxWidth(),
                            message = connection.errorMessage ?: ""
                        )
                    } else {
                        ServerDisconnectedLayout(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            onConnect = {
                                viewModel.connect()
                            }
                        )
                    }
                }
            }
        }
    }
}
