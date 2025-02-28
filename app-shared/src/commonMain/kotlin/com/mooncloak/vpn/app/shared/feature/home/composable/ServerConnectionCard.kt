package com.mooncloak.vpn.app.shared.feature.home.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mooncloak.vpn.api.shared.server.ConnectionType
import com.mooncloak.vpn.app.shared.composable.CloakedLayout
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.action_connect
import com.mooncloak.vpn.app.shared.resource.action_disconnect
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import com.mooncloak.vpn.app.shared.util.icon
import com.mooncloak.vpn.app.shared.util.title
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ServerConnectionCard(
    label: String? = null,
    countryName: String?,
    countryFlag: String?,
    serverName: String,
    connectionType: ConnectionType?,
    connected: Boolean,
    connectionTime: Instant?,
    onConnect: () -> Unit,
    onDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onDetails
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (connected) {
                CloakedLayout(
                    modifier = Modifier.fillMaxWidth()
                        .aspectRatio(1f),
                    since = connectionTime ?: Clock.System.now()
                )
            }

            if (!connected && !label.isNullOrBlank()) {
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp),
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            ListItem(
                modifier = Modifier.fillMaxWidth(),
                leadingContent = (@Composable {
                    if (countryFlag != null) {
                        AsyncImage(
                            modifier = Modifier.size(24.dp),
                            model = countryFlag,
                            contentDescription = null
                        )
                    }
                }).takeIf { countryFlag != null },
                headlineContent = {
                    Text(
                        text = countryName ?: serverName,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                supportingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = serverName,
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = SecondaryAlpha
                                )
                            )
                        )

                        if (connectionType != null) {
                            Row(
                                modifier = Modifier.padding(start = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // TODO: Icon for Connection Type
                                connectionType.icon?.let { icon ->
                                    Icon(
                                        modifier = Modifier.size(12.dp),
                                        painter = icon,
                                        contentDescription = null
                                    )
                                }

                                Text(
                                    text = connectionType.title,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = SecondaryAlpha
                                        )
                                    )
                                )
                            }
                        }
                    }
                },
                trailingContent = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
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
