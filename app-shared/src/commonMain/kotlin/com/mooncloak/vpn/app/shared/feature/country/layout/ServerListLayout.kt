package com.mooncloak.vpn.app.shared.feature.country.layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.network.core.vpn.connectedTo
import com.mooncloak.vpn.network.core.vpn.isToggling
import com.mooncloak.vpn.app.shared.feature.country.composable.ErrorCard
import com.mooncloak.vpn.app.shared.feature.country.composable.Label
import com.mooncloak.vpn.app.shared.feature.country.composable.RegionServerListItem
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.util.LaunchLazyLoader
import com.mooncloak.vpn.network.core.vpn.VPNConnection
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ServerListLayout(
    connection: VPNConnection,
    servers: List<Server>,
    label: String,
    loading: Boolean,
    canAppendMore: Boolean,
    lazyListState: LazyListState,
    errorTitle: String?,
    errorDescription: String? = null,
    onLoadMore: () -> Unit,
    onConnect: (server: Server) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchLazyLoader(
        lazyListState = lazyListState,
        enabled = canAppendMore,
        onLoadMore = {
            onLoadMore.invoke()
        }
    )

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item(key = "ContentLabel") {
            Label(
                modifier = Modifier.sizeIn(maxWidth = 600.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = label
            )
        }

        items(
            items = servers,
            key = { server -> server.id },
            contentType = { "ServerListItem" }
        ) { server ->
            RegionServerListItem(
                modifier = Modifier
                    .sizeIn(maxWidth = 600.dp)
                    .fillMaxWidth()
                    .clickable(enabled = !connection.isToggling()) {
                        onConnect.invoke(server)
                    },
                server = server,
                connected = connection.connectedTo(server)
            )
        }

        if ((servers.isEmpty() && !loading) || (!errorTitle.isNullOrBlank())) {
            item(key = "ErrorCard") {
                ErrorCard(
                    modifier = Modifier.sizeIn(maxWidth = 600.dp)
                        .fillMaxWidth()
                        .padding(12.dp),
                    title = errorTitle ?: stringResource(Res.string.global_unexpected_error),
                    description = errorDescription
                )
            }
        }
    }
}
