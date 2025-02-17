package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_open_dns_servers
import com.mooncloak.vpn.app.shared.resource.settings_group_wire_guard
import com.mooncloak.vpn.app.shared.resource.settings_title_wire_guard_dns_servers
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ColumnScope.SettingsWireGuardGroup(
    dnsServersEnabled: Boolean,
    onOpenDnsServers: () -> Unit
) {
    SettingsGroupLabel(
        modifier = Modifier.padding(horizontal = 16.dp)
            .padding(top = 32.dp),
        text = stringResource(Res.string.settings_group_wire_guard)
    )

    ListItem(
        modifier = Modifier.fillMaxWidth()
            .clickable(enabled = dnsServersEnabled) {
                onOpenDnsServers.invoke()
            },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        headlineContent = {
            Text(text = stringResource(Res.string.settings_title_wire_guard_dns_servers))
        },
        trailingContent = (@Composable {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = stringResource(Res.string.cd_open_dns_servers),
                tint = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = SecondaryAlpha
                )
            )
        }).takeIf { dnsServersEnabled }
    )
}
