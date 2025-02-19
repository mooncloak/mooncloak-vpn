package com.mooncloak.vpn.app.shared.feature.subscription.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.DetailRow
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.subscription_field_expiration
import com.mooncloak.vpn.app.shared.resource.subscription_field_purchased
import com.mooncloak.vpn.app.shared.resource.subscription_field_total_bytes
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DetailsContainer(
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
