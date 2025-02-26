package com.mooncloak.vpn.app.shared.feature.subscription.composable

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_not_available
import com.mooncloak.vpn.app.shared.resource.global_remaining
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PlanUsageCard(
    durationRemaining: String?,
    bytesRemaining: String?,
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
                .height(IntrinsicSize.Max)
                .padding(16.dp)
        ) {
            InfoStatSection(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.global_remaining),
                value = durationRemaining ?: stringResource(Res.string.global_not_available)
            )

            VerticalDivider()

            InfoStatSection(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.global_remaining),
                value = bytesRemaining ?: stringResource(Res.string.global_not_available)
            )
        }
    }
}
