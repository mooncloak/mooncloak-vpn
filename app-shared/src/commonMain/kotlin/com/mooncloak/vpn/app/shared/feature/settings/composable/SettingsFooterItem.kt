package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.app_built_description
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingsFooterItem(
    copyright: String?,
    description: String?,
    modifier: Modifier = Modifier
) {
    if (copyright != null) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp)
                .padding(top = 32.dp, bottom = if (description == null) 16.dp else 0.dp),
            text = copyright,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
            )
        )
    }

    if (description != null) {
        Text(
            modifier = Modifier.padding(top = if (copyright == null) 32.dp else 8.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp),
            text = stringResource(Res.string.app_built_description),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
            )
        )
    }
}
