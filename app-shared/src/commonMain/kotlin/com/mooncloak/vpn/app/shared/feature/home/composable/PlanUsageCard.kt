package com.mooncloak.vpn.app.shared.feature.home.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_not_available
import com.mooncloak.vpn.app.shared.resource.plan_usage_action_boost
import com.mooncloak.vpn.app.shared.resource.plan_usage_label_remaining
import com.mooncloak.vpn.app.shared.resource.plan_usage_title
import com.mooncloak.vpn.app.shared.util.BytesFormatter
import com.mooncloak.vpn.app.shared.util.Default
import com.mooncloak.vpn.app.shared.util.formatWithUnit
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration

@Composable
internal fun PlanUsageCard(
    durationRemaining: Duration?,
    bytesRemaining: Long?,
    boost: Boolean = false,
    onBoost: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            HomeCardHeader(
                modifier = Modifier.fillMaxWidth(),
                leadingText = stringResource(Res.string.plan_usage_title),
                leadingIcon = Icons.Default.DataUsage
            )

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                InfoStatSection(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.plan_usage_label_remaining),
                    value = durationRemaining?.toString() ?: "" // TODO: Format Duration
                )

                InfoStatSection(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.plan_usage_label_remaining),
                    value = bytesRemaining?.let { bytes ->
                        BytesFormatter.Default.formatWithUnit(
                            bytes = bytes,
                            type = BytesFormatter.Type.Bytes
                        )
                    } ?: stringResource(Res.string.global_not_available)
                )
            }

            if (boost) {
                Button(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 16.dp),
                    onClick = onBoost
                ) {
                    Text(
                        text = stringResource(Res.string.plan_usage_action_boost)
                    )
                }
            }
        }
    }
}
