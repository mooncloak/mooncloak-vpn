package com.mooncloak.vpn.app.shared.feature.server.connection.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.server_connection_action_connect
import com.mooncloak.vpn.app.shared.resource.server_connection_disconnected_description
import com.mooncloak.vpn.app.shared.resource.server_connection_disconnected_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ServerDisconnectedLayout(
    onConnect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.server_connection_disconnected_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 16.dp)
                .fillMaxWidth(),
            text = stringResource(Res.string.server_connection_disconnected_description),
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
            onClick = onConnect
        ) {
            Text(
                text = stringResource(Res.string.server_connection_action_connect)
            )
        }
    }
}
