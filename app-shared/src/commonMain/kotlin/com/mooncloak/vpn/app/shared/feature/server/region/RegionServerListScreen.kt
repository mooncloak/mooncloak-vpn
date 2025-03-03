package com.mooncloak.vpn.app.shared.feature.server.region

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.server.region.composable.ServerListItem
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.server_list_header_label
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
public fun RegionServerListScreen(
    state: RegionServerListBottomSheetState,
    onConnect: (server: Server) -> Unit,
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createRegionServerListComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = state.bottomSheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            snackbarHostState = snackbarHostState,
            loadingState = derivedStateOf { viewModel.state.current.value.isLoading }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = lazyListState
            ) {
                item(key = "ServerHeaderLabel") {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(Res.string.server_list_header_label),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = SecondaryAlpha
                            )
                        )
                    )
                }

                items(
                    items = viewModel.state.current.value.servers,
                    key = { server -> server.id },
                    contentType = { "ServerListItem" }
                ) { server ->
                    ServerListItem(
                        modifier = Modifier.fillMaxWidth()
                            .clickable(enabled = server.status?.active == true && server.status?.connectable == true) {
                                onConnect.invoke(server)
                            },
                        server = server
                    )
                }
            }
        }
    }
}
