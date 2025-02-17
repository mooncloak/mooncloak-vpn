package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsDeviceDetails
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_action_hide_ip_address
import com.mooncloak.vpn.app.shared.resource.cd_action_show_ip_address
import com.mooncloak.vpn.app.shared.resource.global_not_available
import com.mooncloak.vpn.app.shared.resource.server_details_hidden_ip_address
import com.mooncloak.vpn.app.shared.resource.settings_device_details_description
import com.mooncloak.vpn.app.shared.resource.settings_device_details_header
import com.mooncloak.vpn.app.shared.resource.settings_device_details_local_ip
import com.mooncloak.vpn.app.shared.resource.settings_device_details_public_ip
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DeviceDetailsBottomSheetLayout(
    details: SettingsDeviceDetails,
    modifier: Modifier = Modifier
) {
    val hidePublicIpAddress = remember { mutableStateOf(true) }
    val hideLocalIpAddress = remember { mutableStateOf(true) }

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
                text = stringResource(Res.string.settings_device_details_header),
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(horizontal = 16.dp),
                text = stringResource(Res.string.settings_device_details_description),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                )
            )

            ListItem(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                headlineContent = {
                    Text(text = stringResource(Res.string.settings_device_details_public_ip))
                },
                supportingContent = {
                    SelectionContainer {
                        Text(
                            text = if (hidePublicIpAddress.value) {
                                stringResource(Res.string.server_details_hidden_ip_address)
                            } else {
                                details.publicIpAddress?.takeIf { it.isNotBlank() }
                                    ?: stringResource(Res.string.global_not_available)
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                        )
                    }
                },
                trailingContent = (@Composable {
                    Icon(
                        modifier = Modifier.size(40.dp)
                            .clip(CircleShape)
                            .clickable {
                                hidePublicIpAddress.value = !hidePublicIpAddress.value
                            }
                            .padding(8.dp),
                        imageVector = if (hidePublicIpAddress.value) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        },
                        contentDescription = if (hidePublicIpAddress.value) {
                            stringResource(Res.string.cd_action_show_ip_address)
                        } else {
                            stringResource(Res.string.cd_action_hide_ip_address)
                        },
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                    )
                }).takeIf { !details.publicIpAddress.isNullOrBlank() }
            )

            ListItem(
                modifier = Modifier.fillMaxWidth(),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                headlineContent = {
                    Text(text = stringResource(Res.string.settings_device_details_local_ip))
                },
                supportingContent = {
                    SelectionContainer {
                        Text(
                            text = if (hideLocalIpAddress.value) {
                                stringResource(Res.string.server_details_hidden_ip_address)
                            } else {
                                details.localIpAddress?.takeIf { it.isNotBlank() }
                                    ?: stringResource(Res.string.global_not_available)
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                        )
                    }
                },
                trailingContent = (@Composable {
                    Icon(
                        modifier = Modifier.size(40.dp)
                            .clip(CircleShape)
                            .clickable {
                                hideLocalIpAddress.value = !hideLocalIpAddress.value
                            }
                            .padding(8.dp),
                        imageVector = if (hideLocalIpAddress.value) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        },
                        contentDescription = if (hideLocalIpAddress.value) {
                            stringResource(Res.string.cd_action_show_ip_address)
                        } else {
                            stringResource(Res.string.cd_action_hide_ip_address)
                        },
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                    )
                }).takeIf { !details.localIpAddress.isNullOrBlank() }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
