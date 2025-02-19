package com.mooncloak.vpn.app.shared.feature.subscription.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.api.billing.ServicePurchaseReceipt
import com.mooncloak.vpn.app.shared.feature.payment.history.composable.ReceiptCard
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.subscription_action_payment_view_all
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PaymentHistoryCard(
    receipt: ServicePurchaseReceipt,
    onViewAll: () -> Unit,
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
        ) {
            ReceiptCard(
                modifier = Modifier.fillMaxWidth(),
                receipt = receipt
            )

            TextButton(
                modifier = Modifier.wrapContentSize()
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp),
                onClick = onViewAll
            ) {
                Text(
                    text = stringResource(Res.string.subscription_action_payment_view_all)
                )
            }
        }
    }
}
