package com.mooncloak.vpn.app.shared.feature.server.connection

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.vpn.VPNConnection
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.server.connection.composable.ServerConnectedLayout
import com.mooncloak.vpn.app.shared.feature.server.connection.composable.ServerConnectingLayout
import com.mooncloak.vpn.app.shared.feature.server.connection.composable.ServerConnectionErrorLayout
import com.mooncloak.vpn.app.shared.feature.server.connection.composable.ServerDisconnectedLayout
import com.mooncloak.vpn.app.shared.feature.server.connection.composable.ServerDisconnectingLayout
import com.mooncloak.vpn.app.shared.feature.server.connection.di.createServerConnectionComponent

@Composable
public fun ServerConnectionScreen(
    server: Server?,
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createServerConnectionComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }

    LaunchedEffect(server) {
        viewModel.load(server = server)
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
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
