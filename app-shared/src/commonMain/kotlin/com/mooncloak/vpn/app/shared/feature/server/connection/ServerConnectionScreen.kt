package com.mooncloak.vpn.app.shared.feature.server.connection

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.api.server.ServerConnection
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.server.connection.composable.ServerConnectedLayout
import com.mooncloak.vpn.app.shared.feature.server.connection.composable.ServerConnectingLayout
import com.mooncloak.vpn.app.shared.feature.server.connection.composable.ServerDisconnectedLayout
import com.mooncloak.vpn.app.shared.feature.server.connection.di.createServerConnectionComponent

@Composable
public fun ServerConnectionScreen(
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createServerConnectionComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        AnimatedContent(
            targetState = viewModel.state.current.value.connection
        ) { connection ->
            when (connection) {
                is ServerConnection.Connecting -> ServerConnectingLayout(
                    modifier = Modifier.fillMaxWidth(),
                    server = connection.server
                )

                is ServerConnection.Connected -> ServerConnectedLayout(
                    modifier = Modifier.fillMaxWidth(),
                    server = connection.server,
                    timestamp = connection.timestamp,
                    rxThroughput = connection.rxThroughput,
                    txThroughput = connection.txThroughput,
                    onDisconnect = {

                    }
                )

                is ServerConnection.Disconnected -> ServerDisconnectedLayout(
                    modifier = Modifier.fillMaxWidth(),
                    onConnect = {

                    }
                )
            }
        }
    }
}
