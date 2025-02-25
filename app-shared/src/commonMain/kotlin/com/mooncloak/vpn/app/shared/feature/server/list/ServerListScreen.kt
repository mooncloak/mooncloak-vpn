package com.mooncloak.vpn.app.shared.feature.server.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.isConnectable
import com.mooncloak.vpn.api.shared.vpn.connectedTo
import com.mooncloak.vpn.api.shared.vpn.isConnected
import com.mooncloak.vpn.app.shared.composable.rememberModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.server.list.composable.NoServersCard
import com.mooncloak.vpn.app.shared.feature.server.list.composable.PreReleaseNoticeCard
import com.mooncloak.vpn.app.shared.feature.server.list.composable.ServerListBottomSheet
import com.mooncloak.vpn.app.shared.feature.server.list.composable.ServerListItem
import com.mooncloak.vpn.app.shared.feature.server.list.model.ServerListBottomSheetDestination
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.destination_main_servers_title
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
public fun ServerListScreen(
    onConnect: (server: Server) -> Unit,
    modifier: Modifier = Modifier,
    containerPaddingValues: PaddingValues = PaddingValues()
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createServerListComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val topAppBarState = rememberTopAppBarState()
    val topAppBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = topAppBarState)
    val bottomSheetState = rememberModalNavigationBottomSheetState<ServerListBottomSheetDestination>()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier.nestedScroll(topAppBarBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            LargeTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                scrollBehavior = topAppBarBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                title = {
                    Text(text = stringResource(Res.string.destination_main_servers_title))
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(key = "TopSpacing") {
                    Spacer(modifier = Modifier.height(containerPaddingValues.calculateTopPadding()))
                }

                if (viewModel.state.current.value.servers.isEmpty() && !viewModel.state.current.value.isLoading) {
                    item(key = "EmptyServerListError") {
                        NoServersCard(
                            modifier = Modifier.sizeIn(maxWidth = 600.dp)
                                .fillMaxWidth()
                                .padding(12.dp)
                        )
                    }
                }

                items(
                    items = viewModel.state.current.value.servers,
                    key = { server -> server.id },
                    contentType = { "ServerListItem" }
                ) { server ->
                    val connection = viewModel.state.current.value.connection

                    ServerListItem(
                        modifier = Modifier.sizeIn(maxWidth = 600.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clickable(enabled = server.isConnectable(hasSubscription = viewModel.state.current.value.hasSubscription())) {
                                onConnect.invoke(server)
                            },
                        server = server,
                        connected = connection.connectedTo(server),
                        onConnect = {
                            coroutineScope.launch {
                                if (server.isConnectable(hasSubscription = viewModel.state.current.value.hasSubscription()) || viewModel.state.current.value.connection.isConnected()) {
                                    bottomSheetState.show(ServerListBottomSheetDestination.ServerConnection(server = server))
                                } else {
                                    bottomSheetState.show(ServerListBottomSheetDestination.Payment)
                                }
                            }
                        },
                        onDetails = {
                            coroutineScope.launch {
                                bottomSheetState.show(ServerListBottomSheetDestination.ServerDetails(server = server))
                            }
                        }
                    )
                }

                if (viewModel.state.current.value.isPreRelease) {
                    item(key = "PreReleaseNoticeCard") {
                        PreReleaseNoticeCard(
                            modifier = Modifier.sizeIn(maxWidth = 600.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                }

                item(key = "BottomSpacing") {
                    Spacer(modifier = Modifier.height(containerPaddingValues.calculateBottomPadding() + 28.dp))
                }
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.Center),
                visible = viewModel.state.current.value.isLoading
            ) {
                CircularProgressIndicator()
            }
        }
    }

    ServerListBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        state = bottomSheetState
    )

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
