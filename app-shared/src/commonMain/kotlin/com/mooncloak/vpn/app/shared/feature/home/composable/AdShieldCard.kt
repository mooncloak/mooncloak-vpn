package com.mooncloak.vpn.app.shared.feature.home.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            HomeCardHeader(
                modifier = Modifier.fillMaxWidth(),
                leadingText = stringResource(Res.string.ad_shield_title),
                trailingText = if (active) {
                    stringResource(Res.string.ad_shield_active)
                } else {
                    stringResource(Res.string.ad_shield_inactive)
                },
                leadingIcon = Icons.Default.ShieldMoon
            )

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                InfoStatSection(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.ad_shield_label_ads_blocked),
                    value = adsBlocked.toString()
                )

                InfoStatSection(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.ad_shield_label_trackers_blocked),
                    value = trackersBlocked.toString()
                )

                InfoStatSection(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.ad_shield_label_data_saved),
                    value = "0Mb" // TODO: Format Bytes
                )
            }
        }
    }
}
