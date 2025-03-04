package com.mooncloak.vpn.app.shared.feature.country.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.app.shared.theme.ColorPalette

@Composable
internal fun RegionServerListItem(
    server: Server,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(text = server.name)
        },
        trailingContent = {
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
            }
        }
    )
}
