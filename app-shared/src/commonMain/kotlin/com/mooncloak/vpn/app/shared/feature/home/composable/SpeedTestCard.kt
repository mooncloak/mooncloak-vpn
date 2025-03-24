package com.mooncloak.vpn.app.shared.feature.home.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_not_available
import com.mooncloak.vpn.app.shared.resource.plan_usage_unlimited
import com.mooncloak.vpn.app.shared.resource.speed_test_label_download
import com.mooncloak.vpn.app.shared.resource.speed_test_label_ping
import com.mooncloak.vpn.app.shared.resource.speed_test_label_upload
import com.mooncloak.vpn.app.shared.resource.speed_test_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SpeedTestCard(
    ping: String?,
    download: String?,
    upload: String?,
    timestamp: String?,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onOpen
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            HomeCardHeader(
                modifier = Modifier.fillMaxWidth(),
                leadingText = stringResource(Res.string.speed_test_title),
                leadingIcon = Icons.Default.DataUsage,
                trailingIcon = Icons.AutoMirrored.Default.ArrowRight
            )

            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(top = 16.dp)
            ) {
                InfoStatSection(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.speed_test_label_ping),
                    value = ping ?: stringResource(Res.string.global_not_available)
                )

                VerticalDivider()

                InfoStatSection(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.speed_test_label_download),
                    value = download ?: stringResource(Res.string.plan_usage_unlimited)
                )

                VerticalDivider()

                InfoStatSection(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.speed_test_label_upload),
                    value = upload ?: stringResource(Res.string.plan_usage_unlimited)
                )
            }

            if (timestamp != null) {
                Text(
                    text = timestamp,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                )
            }
        }
    }
}
