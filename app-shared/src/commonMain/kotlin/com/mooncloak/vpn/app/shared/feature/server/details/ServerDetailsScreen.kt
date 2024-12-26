package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.server.details.composable.CloakedLayout
import com.mooncloak.vpn.app.shared.feature.server.details.composable.IpAddressCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.ServerInfoCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.SpeedCard
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
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.load(server)
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            val visible = remember { mutableStateOf(false) }

            CloakedLayout(
                modifier = Modifier.fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(horizontal = 16.dp)
            )

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

            SpeedCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                downloadBits = 10000,
                uploadBits = 1000
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
}
