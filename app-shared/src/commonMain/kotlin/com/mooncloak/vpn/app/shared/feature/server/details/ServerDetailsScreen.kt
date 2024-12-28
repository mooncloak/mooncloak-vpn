package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.server.details.composable.CloakedLayout
import com.mooncloak.vpn.app.shared.feature.server.details.composable.ConnectingCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.IpAddressCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.ServerInfoCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.ServerLocationCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.SpeedCard
import com.mooncloak.vpn.app.shared.feature.server.details.composable.UsageCard
import com.mooncloak.vpn.app.shared.feature.server.details.di.createServerDetailsComponent
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.server_connection_disconnected_title
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

            ServerLocationCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                countryName = "United States",
                regionName = "Florida",
                serverName = "Florida #1",
                flagImageUri = null
            )

            IpAddressCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
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

            UsageCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp),
                downloadBytes = 10000,
                uploadBytes = 1000
            )

            ServerInfoCard(
                modifier = Modifier.padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                country = "USA",
                region = "Johnson City",
                serverName = "Server-1"
            )

            ConnectingCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            )

            Button(
                modifier = Modifier.sizeIn(maxWidth = 400.dp)
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 32.dp)
                    .padding(horizontal = 16.dp),
                onClick = {}
            ) {
                Text(
                    text = stringResource(Res.string.server_connection_disconnected_title)
                )
            }
        }
    }
}
