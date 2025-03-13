package com.mooncloak.vpn.app.shared.feature.wireguard.dns.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.moon_shield_subtitle
import com.mooncloak.vpn.app.shared.resource.moon_shield_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DnsMoonShieldCard(
    active: Boolean,
    enabled: Boolean = true,
    onActiveChange: (active: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        onClick = {}
    ) {
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            headlineContent = {
                Text(
                    text = stringResource(Res.string.moon_shield_title),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            supportingContent = {
                Text(
                    text = stringResource(Res.string.moon_shield_subtitle),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                )
            },
            trailingContent = {
                Switch(
                    enabled = enabled,
                    checked = active,
                    onCheckedChange = onActiveChange,
                    colors = SwitchDefaults.colors()
                )
            }
        )
    }
}
