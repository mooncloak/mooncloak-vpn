package com.mooncloak.vpn.app.shared.feature.subscription.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.subscription_action_payment_view_all
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PaymentHistoryCard(
    title: String,
    description: String?,
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
            ListItem(
                modifier = Modifier.fillMaxWidth(),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                headlineContent = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                },
                supportingContent = (@Composable {
                    Text(
                        text = description ?: "",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                        )
                    )
                }).takeIf { description != null }
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
