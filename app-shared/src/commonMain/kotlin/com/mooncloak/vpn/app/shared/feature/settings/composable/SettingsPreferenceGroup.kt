package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.settings_description_landing
import com.mooncloak.vpn.app.shared.resource.settings_description_screen_lock
import com.mooncloak.vpn.app.shared.resource.settings_group_preferences
import com.mooncloak.vpn.app.shared.resource.settings_title_landing
import com.mooncloak.vpn.app.shared.resource.settings_title_screen_lock
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Suppress("UnusedReceiverParameter")
@Composable
internal fun ColumnScope.SettingsPreferenceGroup(
    startOnLandingScreen: Boolean,
    isSystemAuthSupported: Boolean,
    requireSystemAuth: Boolean,
    onToggleStartOnLandingScreen: (checked: Boolean) -> Unit,
    onToggleRequireSystemAuth: (checked: Boolean) -> Unit
) {
    SettingsGroupLabel(
        modifier = Modifier.padding(horizontal = 16.dp)
            .padding(top = 32.dp),
        text = stringResource(Res.string.settings_group_preferences)
    )

    ListItem(
        modifier = Modifier.fillMaxWidth(),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        headlineContent = {
            Text(text = stringResource(Res.string.settings_title_landing))
        },
        supportingContent = {
            Text(
                text = stringResource(Res.string.settings_description_landing),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                )
            )
        },
        trailingContent = {
            Switch(
                checked = startOnLandingScreen,
                onCheckedChange = { checked ->
                    onToggleStartOnLandingScreen(checked)
                }
            )
        }
    )

    if (isSystemAuthSupported) {
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            headlineContent = {
                Text(text = stringResource(Res.string.settings_title_screen_lock))
            },
            supportingContent = {
                Text(
                    text = stringResource(Res.string.settings_description_screen_lock),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                    )
                )
            },
            trailingContent = {
                Switch(
                    checked = requireSystemAuth,
                    onCheckedChange = { checked ->
                        onToggleRequireSystemAuth(checked)
                    }
                )
            }
        )
    }
}
