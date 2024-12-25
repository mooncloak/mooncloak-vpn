package com.mooncloak.vpn.app.shared.feature.server.details.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_not_available
import com.mooncloak.vpn.app.shared.resource.server_details_info_field_connected
import com.mooncloak.vpn.app.shared.resource.server_details_info_field_country
import com.mooncloak.vpn.app.shared.resource.server_details_info_field_load
import com.mooncloak.vpn.app.shared.resource.server_details_info_field_performance
import com.mooncloak.vpn.app.shared.resource.server_details_info_field_protocol
import com.mooncloak.vpn.app.shared.resource.server_details_info_field_region
import com.mooncloak.vpn.app.shared.resource.server_details_info_field_server
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ServerInfoCard(
    country: String,
    region: String? = null,
    connected: String? = null,
    serverName: String,
    serverLoad: String? = null,
    protocol: String? = null,
    performance: String? = null,
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
                label = stringResource(Res.string.server_details_info_field_country),
                value = country
            )

            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.server_details_info_field_region),
                value = region ?: stringResource(Res.string.global_not_available)
            )

            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.server_details_info_field_server),
                value = serverName
            )

            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.server_details_info_field_connected),
                value = connected ?: stringResource(Res.string.global_not_available)
            )

            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.server_details_info_field_load),
                value = serverLoad ?: stringResource(Res.string.global_not_available)
            )

            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.server_details_info_field_protocol),
                value = protocol ?: stringResource(Res.string.global_not_available)
            )

            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.server_details_info_field_performance),
                value = performance ?: stringResource(Res.string.global_not_available)
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
