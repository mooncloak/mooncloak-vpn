package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsDeviceDetails
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_not_available
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
                            text = details.publicIpAddress ?: stringResource(Res.string.global_not_available),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                        )
                    }
                }
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
                            text = details.localIpAddress ?: stringResource(Res.string.global_not_available),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
