package com.mooncloak.vpn.app.shared.feature.country.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.GenericHeaderGraphic
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.country_list_error_description_no_vpn_servers
import com.mooncloak.vpn.app.shared.resource.country_list_error_title_no_vpn_servers
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun NoVPNServersCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            GenericHeaderGraphic(
                modifier = Modifier.size(64.dp)
                    .align(Alignment.CenterHorizontally),
                painter = rememberVectorPainter(Icons.Default.Error)
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.country_list_error_title_no_vpn_servers),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(Res.string.country_list_error_description_no_vpn_servers),
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)),
                textAlign = TextAlign.Center
            )
        }
    }
}
