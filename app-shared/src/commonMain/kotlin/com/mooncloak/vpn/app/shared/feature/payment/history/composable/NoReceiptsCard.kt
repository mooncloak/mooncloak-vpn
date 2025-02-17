package com.mooncloak.vpn.app.shared.feature.payment.history.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.payment_history_no_receipts_action_get_service
import com.mooncloak.vpn.app.shared.resource.payment_history_no_receipts_description
import com.mooncloak.vpn.app.shared.resource.payment_history_no_receipts_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun NoReceiptsCard(
    onProtect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.payment_history_no_receipts_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(Res.string.payment_history_no_receipts_description),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(
                        alpha = SecondaryAlpha
                    )
                ),
                textAlign = TextAlign.Center
            )

            Button(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 32.dp, bottom = 16.dp),
                onClick = onProtect
            ) {
                Text(
                    text = stringResource(Res.string.payment_history_no_receipts_action_get_service)
                )
            }
        }
    }
}
