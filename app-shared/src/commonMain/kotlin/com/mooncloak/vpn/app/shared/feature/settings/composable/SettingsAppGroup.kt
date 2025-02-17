package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.OpenInNew
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
import com.mooncloak.vpn.app.shared.resource.cd_link_source_code
import com.mooncloak.vpn.app.shared.resource.cd_open_app_details
import com.mooncloak.vpn.app.shared.resource.cd_open_contributors
import com.mooncloak.vpn.app.shared.resource.cd_open_licenses
import com.mooncloak.vpn.app.shared.resource.settings_group_app
import com.mooncloak.vpn.app.shared.resource.settings_title_app_version
import com.mooncloak.vpn.app.shared.resource.settings_title_code
import com.mooncloak.vpn.app.shared.resource.settings_title_collaborators
import com.mooncloak.vpn.app.shared.resource.settings_title_licenses
import com.mooncloak.vpn.app.shared.theme.LocalThemePreference
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import com.mooncloak.vpn.app.shared.theme.isInDarkTheme
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ColumnScope.SettingsAppGroup(
    appVersion: String?,
    sourceCodeUri: String?,
    appDetailsEnabled: Boolean = false,
    onOpenAppDetails: () -> Unit,
    onOpenDependencyList: () -> Unit,
    onOpenCollaboratorList: () -> Unit,
    uriHandler: UriHandler = LocalUriHandler.current
) {
    SettingsGroupLabel(
        modifier = Modifier.padding(horizontal = 16.dp)
            .padding(top = 32.dp),
        text = stringResource(Res.string.settings_group_app)
    )

    ListItem(
        modifier = Modifier.fillMaxWidth()
            .clickable(enabled = appDetailsEnabled) {
                onOpenAppDetails.invoke()
            },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        headlineContent = {
            Text(text = stringResource(Res.string.settings_title_app_version))
        },
        supportingContent = (@Composable {
            Text(
                text = appVersion ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = if (LocalThemePreference.current.isInDarkTheme()) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    // There's not enough contrast with the yellow text on the white background. So, we use the
                    // darker blue primary text here.
                    MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                }
            )
        }).takeIf { appVersion != null },
        trailingContent = (@Composable {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = stringResource(Res.string.cd_open_app_details),
                tint = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = SecondaryAlpha
                )
            )
        }).takeIf { appDetailsEnabled }
    )

    ListItem(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                onOpenDependencyList.invoke()
            },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        headlineContent = {
            Text(text = stringResource(Res.string.settings_title_licenses))
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

    AnimatedVisibility(
        visible = sourceCodeUri != null
    ) {
        ListItem(
            modifier = Modifier.fillMaxWidth()
                .clickable {
                    sourceCodeUri?.let { uriHandler.openUri(it) }
                },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            headlineContent = {
                Text(text = stringResource(Res.string.settings_title_code))
            },
            trailingContent = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.AutoMirrored.Default.OpenInNew,
                    contentDescription = stringResource(Res.string.cd_link_source_code),
                    tint = MaterialTheme.colorScheme.onBackground.copy(
                        alpha = SecondaryAlpha
                    )
                )
            }
        )
    }

    ListItem(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                onOpenCollaboratorList.invoke()
            },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        headlineContent = {
            Text(text = stringResource(Res.string.settings_title_collaborators))
        },
        trailingContent = {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = stringResource(Res.string.cd_open_contributors),
                tint = MaterialTheme.colorScheme.onBackground.copy(
                    alpha = SecondaryAlpha
                )
            )
        }
    )
}
