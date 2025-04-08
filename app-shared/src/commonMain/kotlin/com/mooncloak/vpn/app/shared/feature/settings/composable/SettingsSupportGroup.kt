package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.clickable
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
import com.mooncloak.vpn.app.shared.resource.cd_open_licenses
import com.mooncloak.vpn.app.shared.resource.settings_description_support
import com.mooncloak.vpn.app.shared.resource.settings_group_support
import com.mooncloak.vpn.app.shared.resource.settings_title_support
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingsSupportGroup(
    onOpenSupport: () -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsGroupLabel(
        modifier = Modifier.padding(horizontal = 16.dp)
            .padding(top = 32.dp),
        text = stringResource(Res.string.settings_group_support)
    )

    ListItem(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                onOpenSupport.invoke()
            },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        headlineContent = {
            Text(text = stringResource(Res.string.settings_title_support))
        },
        supportingContent = {
            Text(
                text = stringResource(Res.string.settings_description_support),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
            )
        },
        trailingContent = {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = stringResource(Res.string.cd_open_licenses),
                tint = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = SecondaryAlpha
                )
            )
        }
    )
}
