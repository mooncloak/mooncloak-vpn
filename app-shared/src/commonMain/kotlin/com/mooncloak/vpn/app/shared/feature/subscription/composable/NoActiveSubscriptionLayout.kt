package com.mooncloak.vpn.app.shared.feature.subscription.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.subscription_action_protect
import com.mooncloak.vpn.app.shared.resource.subscription_description_protect
import com.mooncloak.vpn.app.shared.resource.subscription_label_payment_history
import com.mooncloak.vpn.app.shared.resource.subscription_title_no_active_plan
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun NoActiveSubscriptionLayout(
    lastPaymentTitle: String?,
    lastPaymentDescription: String?,
    onViewPaymentHistory: () -> Unit,
    onProtect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.subscription_title_no_active_plan),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 8.dp),
            text = stringResource(Res.string.subscription_description_protect),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
            )
        )

        if (lastPaymentTitle != null) {
            Text(
                modifier = Modifier.wrapContentSize()
                    .align(Alignment.Start)
                    .padding(top = 32.dp),
                text = stringResource(Res.string.subscription_label_payment_history),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                )
            )

            /* TODO: Re-enable payment history
            PaymentHistoryCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp),
                title = lastPaymentTitle,
                description = lastPaymentDescription,
                onViewAll = onViewPaymentHistory
            )*/
        }

        Button(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            onClick = onProtect
        ) {
            Text(
                text = stringResource(Res.string.subscription_action_protect)
            )
        }
    }
}
