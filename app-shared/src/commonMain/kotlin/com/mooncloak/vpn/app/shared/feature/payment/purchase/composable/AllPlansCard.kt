package com.mooncloak.vpn.app.shared.feature.payment.purchase.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.payment_all_plans_feature_all_servers
import com.mooncloak.vpn.app.shared.resource.payment_all_plans_feature_anonymous_service
import com.mooncloak.vpn.app.shared.resource.payment_all_plans_feature_data_usage
import com.mooncloak.vpn.app.shared.resource.payment_all_plans_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AllPlansCard(
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
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.payment_all_plans_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
            )

            PlanFeatureRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp),
                value = stringResource(Res.string.payment_all_plans_feature_data_usage)
            )

            PlanFeatureRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp),
                value = stringResource(Res.string.payment_all_plans_feature_anonymous_service)
            )

            PlanFeatureRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp),
                value = stringResource(Res.string.payment_all_plans_feature_all_servers)
            )
        }
    }
}
