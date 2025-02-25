package com.mooncloak.vpn.app.shared.feature.server.connection.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.server_connection_action_disconnect
import com.mooncloak.vpn.app.shared.resource.server_connection_connected_description
import com.mooncloak.vpn.app.shared.resource.server_connection_connected_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ServerConnectedLayout(
    server: Server,
    timestamp: Instant,
    onDisconnect: () -> Unit,
    modifier: Modifier = Modifier,
    rxThroughput: Long? = null,
    txThroughput: Long? = null
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.server_connection_connected_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 16.dp)
                .fillMaxWidth(),
            text = stringResource(Res.string.server_connection_connected_description),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = SecondaryAlpha
                )
            ),
            textAlign = TextAlign.Center
        )

        Button(
            modifier = Modifier.padding(vertical = 32.dp)
                .sizeIn(maxWidth = 400.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            onClick = onDisconnect
        ) {
            Text(
                text = stringResource(Res.string.server_connection_action_disconnect)
            )
        }
    }
}
