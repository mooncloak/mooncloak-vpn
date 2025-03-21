package com.mooncloak.vpn.app.shared.feature.server.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.isConnectable
import com.mooncloak.vpn.app.shared.composable.MooncloakSnackbar
import com.mooncloak.vpn.app.shared.composable.TooltipBox
import com.mooncloak.vpn.network.core.vpn.connectedTo
import com.mooncloak.vpn.network.core.vpn.isConnected
import com.mooncloak.vpn.app.shared.composable.rememberManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.composable.showError
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.country.CountryListScreen
import com.mooncloak.vpn.app.shared.feature.payment.purchase.PaymentScreen
import com.mooncloak.vpn.app.shared.feature.payment.purchase.rememberPurchasingState
import com.mooncloak.vpn.app.shared.feature.server.connection.ServerConnectionScreen
import com.mooncloak.vpn.app.shared.feature.server.connection.rememberServerConnectionBottomSheetState
import com.mooncloak.vpn.app.shared.feature.server.details.ServerDetailsScreen
import com.mooncloak.vpn.app.shared.feature.server.details.rememberServerDetailsBottomSheetState
import com.mooncloak.vpn.app.shared.feature.server.list.composable.CountryListCard
import com.mooncloak.vpn.app.shared.feature.server.list.composable.NoServersCard
import com.mooncloak.vpn.app.shared.feature.server.list.composable.PreReleaseNoticeCard
import com.mooncloak.vpn.app.shared.feature.server.list.composable.ServerListItem
import com.mooncloak.vpn.app.shared.model.NotificationStateModel
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_open_locations
import com.mooncloak.vpn.app.shared.resource.destination_main_servers_title
import com.mooncloak.vpn.app.shared.theme.DefaultHorizontalPageSpacing
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
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val topAppBarState = rememberTopAppBarState()
    val topAppBarBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state = topAppBarState)

    val paymentPurchasingState = rememberPurchasingState()
    val paymentBottomSheetState = rememberManagedModalBottomSheetState(
        confirmValueChange = { value ->
            when (value) {
                // Disable closing the bottom sheet when we are purchasing to prevent getting in an invalid state.
                SheetValue.Hidden -> !paymentPurchasingState.purchasing.value
                SheetValue.Expanded -> true
                SheetValue.PartiallyExpanded -> true
            }
        }
    )

    val serverDetailsBottomSheetState = rememberServerDetailsBottomSheetState()
    val serverConnectionBottomSheetState = rememberServerConnectionBottomSheetState()

    val countryListBottomSheetState = rememberManagedModalBottomSheetState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier.nestedScroll(topAppBarBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    MooncloakSnackbar(
                        snackbarData = snackbarData
                    )
                }
            )
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
                },
                actions = {
                    AnimatedVisibility(
                        modifier = Modifier,
                        visible = topAppBarState.collapsedFraction >= 0.9f,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        TooltipBox(
                            text = stringResource(Res.string.cd_open_locations)
                        ) {
                            IconButton(
                                modifier = Modifier.clip(CircleShape),
                                onClick = {
                                    coroutineScope.launch {
                                        countryListBottomSheetState.show()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Public,
                                    contentDescription = stringResource(Res.string.cd_open_locations),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = DefaultHorizontalPageSpacing),
                state = lazyStaggeredGridState,
                columns = StaggeredGridCells.Adaptive(minSize = 300.dp),
                horizontalArrangement = Arrangement.spacedBy(DefaultHorizontalPageSpacing),
                verticalItemSpacing = 12.dp
            ) {
                item(
                    key = "TopSpacing",
                    span = StaggeredGridItemSpan.FullLine
                ) {
                    Spacer(modifier = Modifier.height(containerPaddingValues.calculateTopPadding()))
                }

                item(key = "CountryListItem") {
                    CountryListCard(
                        modifier = Modifier.sizeIn(maxWidth = 600.dp)
                            .fillMaxWidth(),
                        onClick = {
                            coroutineScope.launch {
                                countryListBottomSheetState.show()
                            }
                        }
                    )
                }

                if (viewModel.state.current.value.servers.isEmpty() && !viewModel.state.current.value.isLoading) {
                    item(key = "EmptyServerListError") {
                        NoServersCard(
                            modifier = Modifier.sizeIn(maxWidth = 600.dp)
                                .fillMaxWidth()
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
                            .clickable(enabled = server.isConnectable(hasSubscription = viewModel.state.current.value.hasSubscription())) {
                                onConnect.invoke(server)
                            },
                        server = server,
                        connected = connection.connectedTo(server),
                        onConnect = {
                            coroutineScope.launch {
                                if (server.isConnectable(hasSubscription = viewModel.state.current.value.hasSubscription()) || viewModel.state.current.value.connection.isConnected()) {
                                    serverConnectionBottomSheetState.show(server)
                                } else {
                                    paymentBottomSheetState.show()
                                }
                            }
                        },
                        onDetails = {
                            coroutineScope.launch {
                                serverDetailsBottomSheetState.show(server)
                            }
                        }
                    )
                }

                if (viewModel.state.current.value.isPreRelease) {
                    item(key = "PreReleaseNoticeCard") {
                        PreReleaseNoticeCard(
                            modifier = Modifier.sizeIn(maxWidth = 600.dp)
                                .fillMaxWidth()
                        )
                    }
                }

                item(
                    key = "BottomSpacing",
                    span = StaggeredGridItemSpan.FullLine
                ) {
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

    PaymentScreen(
        modifier = Modifier.fillMaxWidth(),
        sheetState = paymentBottomSheetState,
        purchasingState = paymentPurchasingState,
    )

    ServerConnectionScreen(
        modifier = Modifier.fillMaxWidth(),
        state = serverConnectionBottomSheetState
    )

    ServerDetailsScreen(
        modifier = Modifier.fillMaxWidth(),
        state = serverDetailsBottomSheetState
    )

    CountryListScreen(
        modifier = Modifier.fillMaxSize(),
        sheetState = countryListBottomSheetState,
        onOpenPlans = {
            coroutineScope.launch {
                paymentBottomSheetState.show()
            }
        }
    )

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showError(notification = NotificationStateModel(message = errorMessage))
        }
    }
}
