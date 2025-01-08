package com.mooncloak.vpn.app.shared.feature.server.connection.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.composable.PulsatingDots
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.server_connection_connecting_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ServerConnectingLayout(
    server: Server,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.server_connection_connecting_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 8.dp)
                .fillMaxWidth(),
            text = server.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = SecondaryAlpha
                )
            ),
            textAlign = TextAlign.Center
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 32.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(36.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Default.Devices,
                contentDescription = null
            )

            PulsatingDots(
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Icon(
                modifier = Modifier.size(36.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Default.VpnLock,
                contentDescription = null
            )
        }
    }
}
