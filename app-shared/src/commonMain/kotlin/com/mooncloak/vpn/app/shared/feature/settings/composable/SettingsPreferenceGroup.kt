package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.settings_description_landing
import com.mooncloak.vpn.app.shared.resource.settings_description_screen_lock
import com.mooncloak.vpn.app.shared.resource.settings_group_preferences
import com.mooncloak.vpn.app.shared.resource.settings_title_landing
import com.mooncloak.vpn.app.shared.resource.settings_title_screen_lock
import com.mooncloak.vpn.app.shared.resource.settings_title_screen_lock_timeout
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

@Suppress("UnusedReceiverParameter")
@Composable
internal fun ColumnScope.SettingsPreferenceGroup(
    startOnLandingScreen: Boolean,
    isSystemAuthSupported: Boolean,
    requireSystemAuth: Boolean,
    systemAuthTimeout: Duration,
    onToggleStartOnLandingScreen: (checked: Boolean) -> Unit,
    onToggleRequireSystemAuth: (checked: Boolean) -> Unit,
    onSystemAuthTimeoutChanged: (timeout: Duration) -> Unit
) {
    val systemAuthTimeoutDropDownExpanded = remember { mutableStateOf(false) }

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

        AnimatedVisibility(
            visible = requireSystemAuth
        ) {
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                headlineContent = {
                    Text(text = stringResource(Res.string.settings_title_screen_lock_timeout))
                },
                trailingContent = {
                    Box {
                        Row(
                            modifier = Modifier.clickable {
                                systemAuthTimeoutDropDownExpanded.value = !systemAuthTimeoutDropDownExpanded.value
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = systemAuthTimeout.toString(
                                    unit = DurationUnit.MINUTES,
                                    decimals = 0
                                ),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                                )
                            )

                            Icon(
                                modifier = Modifier.padding(start = 16.dp)
                                    .size(24.dp),
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                            )
                        }

                        DurationDropdown(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            value = systemAuthTimeout,
                            durations = setOf(
                                1.minutes,
                                5.minutes,
                                30.minutes
                            ),
                            expanded = systemAuthTimeoutDropDownExpanded.value,
                            onDismissRequest = {
                                systemAuthTimeoutDropDownExpanded.value = false
                            },
                            onValueChanged = onSystemAuthTimeoutChanged
                        )
                    }
                }
            )
        }
    }
}
