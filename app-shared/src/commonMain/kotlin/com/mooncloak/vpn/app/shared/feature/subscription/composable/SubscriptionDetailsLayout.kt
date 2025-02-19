package com.mooncloak.vpn.app.shared.feature.subscription.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_not_available
import com.mooncloak.vpn.app.shared.resource.subscription_label_plan_details
import com.mooncloak.vpn.app.shared.resource.subscription_label_usage
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SubscriptionDetailsLayout(
    planTitle: String?,
    subscriptionPurchased: String?,
    subscriptionExpiration: String?,
    subscriptionTotalData: String?,
    subscriptionRemainingDuration: String?,
    subscriptionRemainingData: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SubscriptionSection(
            modifier = Modifier.wrapContentSize()
                .align(Alignment.Start),
            label = stringResource(Res.string.subscription_label_plan_details)
        ) {
            DetailsContainer(
                modifier = Modifier.fillMaxWidth(),
                name = planTitle?.takeIf { it.isNotBlank() } ?: stringResource(Res.string.global_not_available),
                purchased = subscriptionPurchased?.takeIf { it.isNotBlank() }
                    ?: stringResource(Res.string.global_not_available),
                expiration = subscriptionExpiration?.takeIf { it.isNotBlank() }
                    ?: stringResource(Res.string.global_not_available),
                totalBytes = subscriptionTotalData?.takeIf { it.isNotBlank() }
                    ?: stringResource(Res.string.global_not_available)
            )
        }

        SubscriptionSection(
            modifier = Modifier.wrapContentSize()
                .align(Alignment.Start)
                .padding(top = 32.dp),
            label = stringResource(Res.string.subscription_label_usage)
        ) {
            PlanUsageCard(
                modifier = Modifier.fillMaxWidth(),
                durationRemaining = subscriptionRemainingDuration ?: stringResource(Res.string.global_not_available),
                bytesRemaining = subscriptionRemainingData ?: stringResource(Res.string.global_not_available)
            )
        }
    }
}
