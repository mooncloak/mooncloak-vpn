package com.mooncloak.vpn.app.shared.feature.server.list.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.action_connect
import com.mooncloak.vpn.app.shared.resource.action_disconnect
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ServerListItem(
    server: Server,
    connected: Boolean,
    onConnect: () -> Unit,
    onDetails: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onDetails
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                leadingContent = (@Composable {
                    AsyncImage(
                        modifier = Modifier.size(24.dp),
                        model = server.country?.flag,
                        contentDescription = null
                    )
                }).takeIf { server.country?.flag != null },
                headlineContent = {
                    Text(
                        text = buildString {
                            if (server.country != null || server.region != null) {
                                server.country?.name?.let { countryName -> append(countryName) }
                                server.region?.name?.let { regionName ->
                                    if (server.country != null) {
                                        append(" - $regionName")
                                    } else {
                                        append(regionName)
                                    }
                                }
                            } else {
                                append(server.name)
                            }
                        },
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                supportingContent = (@Composable {
                    Column {
                        Text(
                            text = server.name,
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = contentColor.copy(
                                    alpha = SecondaryAlpha
                                )
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            server.tags.forEach { tag ->
                                AssistChip(
                                    onClick = {},
                                    enabled = false,
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = Color.Transparent,
                                        labelColor = contentColor
                                    ),
                                    label = {
                                        Text(text = tag)
                                    }
                                )
                            }
                        }
                    }
                }).takeIf { server.country != null || server.region != null || server.tags.isNotEmpty() },
                trailingContent = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = null,
                        tint = contentColor.copy(alpha = SecondaryAlpha)
                    )
                }
            )

            Button(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (connected) {
                        MaterialTheme.colorScheme.errorContainer
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    contentColor = if (connected) {
                        MaterialTheme.colorScheme.onErrorContainer
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    }
                ),
                onClick = onConnect
            ) {
                Text(
                    text = if (connected) {
                        stringResource(Res.string.action_disconnect)
                    } else {
                        stringResource(Res.string.action_connect)
                    }
                )
            }
        }
    }
}
