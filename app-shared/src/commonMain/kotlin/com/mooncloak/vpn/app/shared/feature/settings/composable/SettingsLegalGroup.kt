package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_link_privacy_policy
import com.mooncloak.vpn.app.shared.resource.cd_link_terms
import com.mooncloak.vpn.app.shared.resource.settings_group_legal
import com.mooncloak.vpn.app.shared.resource.settings_title_privacy_policy
import com.mooncloak.vpn.app.shared.resource.settings_title_terms
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ColumnScope.SettingsLegalGroup(
    privacyPolicyUri: String?,
    termsUri: String?,
    uriHandler: UriHandler = LocalUriHandler.current
) {
    if (privacyPolicyUri != null || termsUri != null) {
        SettingsGroupLabel(
            modifier = Modifier.padding(horizontal = 16.dp)
                .padding(top = 32.dp),
            text = stringResource(Res.string.settings_group_legal)
        )

        AnimatedVisibility(
            visible = privacyPolicyUri != null
        ) {
            ListItem(
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        privacyPolicyUri?.let { uriHandler.openUri(it) }
                    },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                headlineContent = {
                    Text(text = stringResource(Res.string.settings_title_privacy_policy))
                },
                trailingContent = {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.AutoMirrored.Default.OpenInNew,
                        contentDescription = stringResource(Res.string.cd_link_privacy_policy),
                        tint = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = SecondaryAlpha
                        )
                    )
                }
            )
        }

        AnimatedVisibility(
            visible = termsUri != null
        ) {
            ListItem(
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        termsUri?.let { uriHandler.openUri(it) }
                    },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                headlineContent = {
                    Text(text = stringResource(Res.string.settings_title_terms))
                },
                trailingContent = {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.AutoMirrored.Default.OpenInNew,
                        contentDescription = stringResource(Res.string.cd_link_terms),
                        tint = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = SecondaryAlpha
                        )
                    )
                }
            )
        }
    }
}
