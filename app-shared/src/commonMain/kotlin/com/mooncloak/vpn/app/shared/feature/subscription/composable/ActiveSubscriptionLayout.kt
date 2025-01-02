package com.mooncloak.vpn.app.shared.feature.subscription.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_not_available
import com.mooncloak.vpn.app.shared.resource.plan_usage_label_remaining
import com.mooncloak.vpn.app.shared.resource.subscription_action_boost
import com.mooncloak.vpn.app.shared.resource.subscription_field_expiration
import com.mooncloak.vpn.app.shared.resource.subscription_field_purchased
import com.mooncloak.vpn.app.shared.resource.subscription_field_total_bytes
import com.mooncloak.vpn.app.shared.resource.subscription_label_payment_history
import com.mooncloak.vpn.app.shared.resource.subscription_label_plan_details
import com.mooncloak.vpn.app.shared.resource.subscription_label_usage
import com.mooncloak.vpn.app.shared.resource.subscription_title_active_plan
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ActiveSubscriptionLayout(
    planTitle: String,
    planDescription: String?,
    subscriptionPurchased: String?,
    subscriptionExpiration: String?,
    subscriptionTotalData: String?,
    subscriptionRemainingDuration: String?,
    subscriptionRemainingData: String?,
    lastPaymentTitle: String?,
    lastPaymentDescription: String?,
    onViewPaymentHistory: () -> Unit,
    onBoost: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.subscription_title_active_plan),
            style = MaterialTheme.typography.titleLarge
        )

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = planTitle,
                style = MaterialTheme.typography.titleMedium
            )

            planDescription?.let { description ->
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 8.dp),
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                    )
                )
            }
        }

        Text(
            modifier = Modifier.wrapContentSize()
                .align(Alignment.Start)
                .padding(top = 32.dp),
            text = stringResource(Res.string.subscription_label_plan_details),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
            )
        )

        DetailsContainer(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp),
            purchased = subscriptionPurchased ?: stringResource(Res.string.global_not_available),
            expiration = subscriptionExpiration ?: stringResource(Res.string.global_not_available),
            totalBytes = subscriptionTotalData ?: stringResource(Res.string.global_not_available)
        )

        Text(
            modifier = Modifier.wrapContentSize()
                .align(Alignment.Start)
                .padding(top = 32.dp),
            text = stringResource(Res.string.subscription_label_usage),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
            )
        )

        PlanUsageCard(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp),
            durationRemaining = subscriptionRemainingDuration ?: stringResource(Res.string.global_not_available),
            bytesRemaining = subscriptionRemainingData ?: stringResource(Res.string.global_not_available)
        )

        /* TODO: Re-enable payment history
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

            PaymentHistoryCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp),
                title = lastPaymentTitle,
                description = lastPaymentDescription,
                onViewAll = onViewPaymentHistory
            )
        }*/

        /* TODO: Re-enable boost
        Button(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            onClick = onBoost
        ) {
            Text(
                text = stringResource(Res.string.subscription_action_boost)
            )
        }*/
    }
}

@Composable
private fun DetailsContainer(
    purchased: String,
    expiration: String,
    totalBytes: String,
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
                .padding(16.dp)
        ) {
            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.subscription_field_purchased),
                value = purchased
            )

            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.subscription_field_expiration),
                value = expiration
            )

            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.subscription_field_total_bytes),
                value = totalBytes
            )
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.wrapContentSize(),
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier.wrapContentSize(),
            text = value,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun PlanUsageCard(
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
                label = stringResource(Res.string.plan_usage_label_remaining),
                value = durationRemaining ?: stringResource(Res.string.global_not_available)
            )

            VerticalDivider()

            InfoStatSection(
                modifier = Modifier.weight(1f),
                label = stringResource(Res.string.plan_usage_label_remaining),
                value = bytesRemaining ?: stringResource(Res.string.global_not_available)
            )
        }
    }
}

@Composable
private fun InfoStatSection(
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
