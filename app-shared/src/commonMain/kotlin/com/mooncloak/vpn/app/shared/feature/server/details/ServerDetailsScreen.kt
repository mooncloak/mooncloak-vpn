package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.server.details.composable.IpAddressCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.ServerInfoCard
import com.mooncloak.vpn.app.shared.feature.server.details.di.createServerDetailsComponent

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
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.load(server)
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column {
            val visible = remember { mutableStateOf(false) }

            IpAddressCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                deviceIpAddress = "146.70.228.142",
                serverIpAddress = "146.70.228.142",
                hideDeviceIpAddress = visible.value,
                onHideDeviceIpAddressChanged = {
                    visible.value = !visible.value
                }
            )

            ServerInfoCard(
                modifier = Modifier.padding(16.dp)
                    .fillMaxWidth(),
                country = "USA",
                region = "Johnson City",
                serverName = "Server-1"
            )
        }
    }

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
