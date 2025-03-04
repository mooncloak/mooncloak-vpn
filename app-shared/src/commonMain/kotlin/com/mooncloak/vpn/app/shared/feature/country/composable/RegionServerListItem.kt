package com.mooncloak.vpn.app.shared.feature.country.composable

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.server.Server

@Composable
internal fun RegionServerListItem(
    server: Server,
    connected: Boolean = false,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    accentColor: Color = MaterialTheme.colorScheme.primary
) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(text = server.name)
        },
        trailingContent = {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.PowerSettingsNew,
                contentDescription = null,
                tint = if (connected) accentColor else contentColor
            )

            /* TODO: Re-enable when server load is supported.
            Row(verticalAlignment = Alignment.CenterVertically) {
                val load = server.status?.load ?: 0.5f

                LinearProgressIndicator(
                    modifier = Modifier.width(100.dp),
                    progress = { server.status?.load ?: 0.5f },
                    color = when {
                        load < 0.8f -> ColorPalette.Teal_500
                        load < 0.9f -> ColorPalette.MooncloakYellow
                        else -> ColorPalette.MooncloakError
                    }
                )
            }*/
        }
    )
}
