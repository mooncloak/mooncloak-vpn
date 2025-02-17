package com.mooncloak.vpn.app.shared.feature.server.details.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_action_hide_ip_address
import com.mooncloak.vpn.app.shared.resource.cd_action_show_ip_address
import com.mooncloak.vpn.app.shared.resource.server_details_hidden_ip_address
import com.mooncloak.vpn.app.shared.resource.server_details_label_device_ip_address
import com.mooncloak.vpn.app.shared.resource.server_details_label_server_ip_address
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun IpAddressCard(
    deviceIpAddress: String,
    serverIpAddress: String,
    hideDeviceIpAddress: Boolean,
    onHideDeviceIpAddressChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
                    .clip(CardDefaults.shape)
                    .clickable { onHideDeviceIpAddressChanged.invoke() },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.server_details_label_device_ip_address),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(
                                alpha = 0.68f
                            )
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center
                    )

                    Icon(
                        modifier = Modifier.padding(start = 8.dp)
                            .size(12.dp),
                        imageVector = if (hideDeviceIpAddress) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        },
                        contentDescription = if (hideDeviceIpAddress) {
                            stringResource(Res.string.cd_action_show_ip_address)
                        } else {
                            stringResource(Res.string.cd_action_hide_ip_address)
                        },
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                    )
                }

                Text(
                    text = if (hideDeviceIpAddress) {
                        stringResource(Res.string.server_details_hidden_ip_address)
                    } else {
                        deviceIpAddress
                    },
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }

            Icon(
                modifier = Modifier.padding(horizontal = 8.dp)
                    .size(18.dp),
                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = if (hideDeviceIpAddress) {
                    stringResource(Res.string.cd_action_show_ip_address)
                } else {
                    stringResource(Res.string.cd_action_hide_ip_address)
                },
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
            )

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = stringResource(Res.string.server_details_label_server_ip_address),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.68f
                        )
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = serverIpAddress,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
