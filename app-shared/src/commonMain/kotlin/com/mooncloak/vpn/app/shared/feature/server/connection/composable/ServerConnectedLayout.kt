package com.mooncloak.vpn.app.shared.feature.server.connection.composable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.api.server.Server
import kotlinx.datetime.Instant

@Composable
internal fun ServerConnectedLayout(
    server: Server,
    timestamp: Instant,
    onDisconnect: () -> Unit,
    modifier: Modifier = Modifier,
    rxThroughput: Long? = null,
    txThroughput: Long? = null
) {

}
