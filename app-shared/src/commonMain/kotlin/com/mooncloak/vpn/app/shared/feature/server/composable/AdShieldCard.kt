package com.mooncloak.vpn.app.shared.feature.server.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.ad_shield_active
import com.mooncloak.vpn.app.shared.resource.ad_shield_inactive
import com.mooncloak.vpn.app.shared.resource.ad_shield_label_ads_blocked
import com.mooncloak.vpn.app.shared.resource.ad_shield_label_data_saved
import com.mooncloak.vpn.app.shared.resource.ad_shield_label_trackers_blocked
import com.mooncloak.vpn.app.shared.resource.ad_shield_title
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AdShieldCard(
    adsBlocked: Int,
    trackersBlocked: Int,
    bytesSaved: Long,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.ShieldMoon,
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = stringResource(Res.string.ad_shield_title),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = if (active) {
                        stringResource(Res.string.ad_shield_active)
                    } else {
                        stringResource(Res.string.ad_shield_inactive)
                    },
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.68f
                        )
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                InfoSection(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.ad_shield_label_ads_blocked),
                    value = adsBlocked.toString()
                )

                InfoSection(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.ad_shield_label_trackers_blocked),
                    value = trackersBlocked.toString()
                )

                InfoSection(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.ad_shield_label_data_saved),
                    value = "0Mb" // TODO: Format Bytes
                )
            }
        }
    }
}

@Composable
private fun InfoSection(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)),
            maxLines = 2,
            overflow = TextOverflow.Clip,
            textAlign = TextAlign.Center
        )
    }
}
