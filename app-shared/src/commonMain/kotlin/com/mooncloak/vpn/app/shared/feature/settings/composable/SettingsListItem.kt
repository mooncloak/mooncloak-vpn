package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha

@Composable
internal fun SettingsListItem(
    headlineText: String,
    supportingText: String? = null,
    icon: Painter,
    modifier: Modifier = Modifier
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Icon(
                modifier = Modifier.size(36.dp),
                painter = icon,
                contentDescription = null
            )
        },
        headlineContent = {
            Text(text = headlineText)
        },
        supportingContent = (@Composable {
            Text(
                text = supportingText ?: "",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha))
            )
        }).takeIf { supportingText != null }
    )
}
