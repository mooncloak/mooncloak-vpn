package com.mooncloak.vpn.app.shared.feature.payment.history.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.api.billing.ServicePurchaseReceipt
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import com.mooncloak.vpn.app.shared.util.format
import com.mooncloak.vpn.app.shared.util.time.DateTimeFormatter
import com.mooncloak.vpn.app.shared.util.time.Full
import com.mooncloak.vpn.app.shared.util.time.format

@Composable
internal fun ReceiptCard(
    receipt: ServicePurchaseReceipt,
    modifier: Modifier = Modifier,
    dateTimeFormatter: DateTimeFormatter = remember { DateTimeFormatter.Full }
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                headlineContent = {
                    Text(
                        text = dateTimeFormatter.format(receipt.purchased),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                supportingContent = {
                    Text(
                        text = "${receipt.provider.value} - …***${receipt.token.value.takeLast(8)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                    )
                },
                trailingContent = (@Composable {
                    Text(
                        text = receipt.price?.format() ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }).takeIf { receipt.price != null }
            )
        }
    }
}
