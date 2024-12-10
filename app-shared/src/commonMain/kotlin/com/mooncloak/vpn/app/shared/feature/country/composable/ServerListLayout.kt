package com.mooncloak.vpn.app.shared.feature.country.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.api.Server
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.country_list_header_label_with_count
import com.mooncloak.vpn.app.shared.resource.server_list_header_label
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ServerListLayout(
    servers: List<Server>,
    onConnect: (server: Server) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item(key = "ServerHeaderLabel") {
            CountryListHeaderLabel(
                text = stringResource(
                    Res.string.country_list_header_label_with_count,
                    stringResource(Res.string.server_list_header_label),
                    servers.size
                )
            )
        }

        items(
            items = servers,
            key = { server -> server.id },
            contentType = { "ServerListItem" }
        ) { server ->
            ServerListItem(
                modifier = Modifier.fillMaxWidth()
                    .clickable(enabled = server.status.active && server.status.connectable) {
                        onConnect.invoke(server)
                    },
                server = server
            )
        }
    }
}
