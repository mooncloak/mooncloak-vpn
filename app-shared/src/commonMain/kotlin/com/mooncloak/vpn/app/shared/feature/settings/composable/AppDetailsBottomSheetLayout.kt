package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsAppDetails
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_not_available
import com.mooncloak.vpn.app.shared.resource.global_yes
import com.mooncloak.vpn.app.shared.resource.settings_app_details_header
import com.mooncloak.vpn.app.shared.resource.settings_app_details_title_build_time
import com.mooncloak.vpn.app.shared.resource.settings_app_details_title_debug
import com.mooncloak.vpn.app.shared.resource.settings_app_details_title_id
import com.mooncloak.vpn.app.shared.resource.settings_app_details_title_name
import com.mooncloak.vpn.app.shared.resource.settings_app_details_title_pre_release
import com.mooncloak.vpn.app.shared.resource.settings_app_details_title_version
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import com.mooncloak.vpn.app.shared.util.time.DateTimeFormatter
import com.mooncloak.vpn.app.shared.util.time.Full
import com.mooncloak.vpn.app.shared.util.time.format
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AppDetailsBottomSheetLayout(
    details: SettingsAppDetails,
    modifier: Modifier = Modifier,
    dateTimeFormatter: DateTimeFormatter = remember { DateTimeFormatter.Full }
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                text = stringResource(Res.string.settings_app_details_header),
                style = MaterialTheme.typography.titleLarge
            )

            ListItem(
                modifier = Modifier.fillMaxWidth(),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                headlineContent = {
                    Text(text = stringResource(Res.string.settings_app_details_title_id))
                },
                supportingContent = {
                    Text(
                        text = details.id,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                    )
                }
            )

            ListItem(
                modifier = Modifier.fillMaxWidth(),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                headlineContent = {
                    Text(text = stringResource(Res.string.settings_app_details_title_name))
                },
                supportingContent = {
                    Text(
                        text = details.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                    )
                }
            )

            ListItem(
                modifier = Modifier.fillMaxWidth(),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                headlineContent = {
                    Text(text = stringResource(Res.string.settings_app_details_title_version))
                },
                supportingContent = {
                    Text(
                        text = details.version,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                    )
                }
            )

            if (details.isDebug) {
                ListItem(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    headlineContent = {
                        Text(text = stringResource(Res.string.settings_app_details_title_debug))
                    },
                    supportingContent = {
                        Text(
                            text = stringResource(Res.string.global_yes),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                        )
                    }
                )
            }

            if (details.isPreRelease) {
                ListItem(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    headlineContent = {
                        Text(text = stringResource(Res.string.settings_app_details_title_pre_release))
                    },
                    supportingContent = {
                        Text(
                            text = stringResource(Res.string.global_yes),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                        )
                    }
                )
            }

            ListItem(
                modifier = Modifier.fillMaxWidth(),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                headlineContent = {
                    Text(text = stringResource(Res.string.settings_app_details_title_build_time))
                },
                supportingContent = {
                    Text(
                        text = details.buildTime?.let { dateTimeFormatter.format(it) }
                            ?: stringResource(Res.string.global_not_available),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                    )
                }
            )
        }
    }
}
