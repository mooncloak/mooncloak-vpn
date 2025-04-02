package com.mooncloak.vpn.app.shared.feature.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.server.VPNConnectionStatus
import com.mooncloak.vpn.app.shared.composable.MooncloakSnackbar
import com.mooncloak.vpn.app.shared.composable.rememberManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.composable.showError
import com.mooncloak.vpn.app.shared.composable.showSuccess
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.home.composable.HomeTitleBar
import com.mooncloak.vpn.app.shared.feature.home.composable.PlanUsageCard
import com.mooncloak.vpn.app.shared.feature.home.composable.ServerConnectionCard
import com.mooncloak.vpn.app.shared.feature.home.composable.MoonShieldCard
import com.mooncloak.vpn.app.shared.feature.home.composable.GetVPNServiceCard
import com.mooncloak.vpn.app.shared.feature.home.composable.LunarisWalletCard
import com.mooncloak.vpn.app.shared.feature.home.composable.ShowcaseCard
import com.mooncloak.vpn.app.shared.feature.home.composable.SpeedTestCard
import com.mooncloak.vpn.app.shared.feature.home.layout.MoonShieldDescriptionBottomSheet
import com.mooncloak.vpn.app.shared.feature.home.model.HomeFeedItem
import com.mooncloak.vpn.app.shared.feature.payment.purchase.PaymentScreen
import com.mooncloak.vpn.app.shared.feature.payment.purchase.rememberPurchasingState
import com.mooncloak.vpn.app.shared.feature.server.details.ServerDetailsScreen
import com.mooncloak.vpn.app.shared.feature.server.details.rememberServerDetailsBottomSheetState
import com.mooncloak.vpn.app.shared.theme.DefaultHorizontalPageSpacing
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.launch

@Composable
public fun HomeScreen(
    modifier: Modifier = Modifier,
    containerPaddingValues: PaddingValues = PaddingValues()
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createHomeComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val coroutineScope = rememberCoroutineScope()

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
    val moonShieldBottomSheetState = rememberManagedModalBottomSheetState()

    val hazeState = remember { HazeState() }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    // TODOS:
    // * Starred VPN service card

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier.padding(bottom = 28.dp + containerPaddingValues.calculateBottomPadding()),
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    MooncloakSnackbar(
                        snackbarData = snackbarData
                    )
                }
            )
        },
        topBar = {
            HomeTitleBar(
                modifier = Modifier.animateContentSize()
                    .fillMaxWidth()
                    .shadow(elevation = 8.dp),
                status = viewModel.state.current.value.connectionStatus,
                connectedName = viewModel.state.current.value.connectedName,
                ipAddress = viewModel.state.current.value.connectedIpAddress,
                publicIpAddress = viewModel.state.current.value.connectionStatus != VPNConnectionStatus.Connected,
                hazeState = hazeState
            )
        }
    ) { paddingValues ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier.fillMaxSize()
                .hazeSource(state = hazeState)
                .padding(horizontal = 12.dp),
            state = lazyStaggeredGridState,
            columns = StaggeredGridCells.Adaptive(minSize = 300.dp),
            horizontalArrangement = Arrangement.spacedBy(DefaultHorizontalPageSpacing),
            verticalItemSpacing = 12.dp
        ) {
            item(
                key = "TopSpacing",
                span = StaggeredGridItemSpan.FullLine
            ) {
                Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + containerPaddingValues.calculateTopPadding()))
            }

            items(
                items = viewModel.state.current.value.items,
                key = { item -> item.id },
                contentType = { item -> item.contentType }
            ) { item ->
                when (item) {
                    is HomeFeedItem.MoonShieldItem -> MoonShieldCard(
                        modifier = Modifier.sizeIn(maxWidth = 600.dp)
                            .fillMaxWidth()
                            .animateItem(),
                        active = item.active,
                        enabled = item.toggleEnabled,
                        trackersBlocked = item.trackersBlocked,
                        timeSaved = item.estimatedTimeSaved,
                        bytesSaved = item.estimatedBytesSaved,
                        onClick = {
                            coroutineScope.launch {
                                moonShieldBottomSheetState.show()
                            }
                        },
                        onActiveChange = viewModel::toggleMoonShield
                    )

                    HomeFeedItem.GetVPNServiceItem -> GetVPNServiceCard(
                        modifier = Modifier.sizeIn(maxWidth = 600.dp)
                            .fillMaxWidth()
                            .animateItem(),
                        onClick = {
                            coroutineScope.launch {
                                paymentBottomSheetState.show()
                            }
                        }
                    )

                    HomeFeedItem.LunarisWallet -> LunarisWalletCard(
                        modifier = Modifier.sizeIn(maxWidth = 600.dp)
                            .fillMaxWidth()
                            .animateItem(),
                        onClick = {
                            coroutineScope.launch {
                                // TODO: Open Lunaris Wallet Screen
                            }
                        }
                    )

                    is HomeFeedItem.PlanUsageItem -> PlanUsageCard(
                        modifier = Modifier.sizeIn(maxWidth = 600.dp)
                            .fillMaxWidth()
                            .animateItem(),
                        durationRemaining = item.durationRemaining,
                        bytesRemaining = item.bytesRemaining,
                        boost = item.showBoost,
                        onBoost = {
                            coroutineScope.launch {
                                paymentBottomSheetState.show()
                            }
                        }
                    )

                    is HomeFeedItem.ServerConnectionItem -> {
                        val tunnel = item.connection.tunnels.firstOrNull { it.server != null }
                        val server = tunnel?.server

                        if (server != null) {
                            ServerConnectionCard(
                                modifier = Modifier.sizeIn(maxWidth = 600.dp)
                                    .fillMaxWidth()
                                    .animateItem(),
                                countryName = server.country?.name,
                                countryFlag = server.country?.flag,
                                serverName = server.name,
                                connectionType = server.connectionTypes.firstOrNull(),
                                connected = true,
                                connectionTime = item.connection.timestamp,
                                onConnect = {
                                    viewModel.toggleConnection(server = server)
                                },
                                onDetails = {
                                    coroutineScope.launch {
                                        serverDetailsBottomSheetState.show(server)
                                    }
                                }
                            )
                        }
                    }

                    is HomeFeedItem.ServerItem -> ServerConnectionCard(
                        modifier = Modifier.sizeIn(maxWidth = 600.dp)
                            .fillMaxWidth()
                            .animateItem(),
                        label = item.label,
                        countryName = item.server.country?.name,
                        countryFlag = item.server.country?.flag,
                        serverName = item.server.name,
                        connectionType = item.server.connectionTypes.firstOrNull(),
                        connected = item.connected,
                        connectionTime = null,
                        onConnect = {
                            viewModel.toggleConnection(server = item.server)
                        },
                        onDetails = {
                            coroutineScope.launch {
                                serverDetailsBottomSheetState.show(item.server)
                            }
                        }
                    )

                    is HomeFeedItem.ShowcaseItem -> ShowcaseCard(
                        modifier = Modifier.sizeIn(maxWidth = 600.dp)
                            .fillMaxWidth()
                            .animateItem(),
                        icon = item.icon.invoke(),
                        title = item.title.invoke(),
                        description = item.description.invoke()
                    )

                    is HomeFeedItem.SpeedTestItem -> SpeedTestCard(
                        modifier = Modifier.sizeIn(maxWidth = 600.dp)
                            .fillMaxWidth()
                            .animateItem(),
                        ping = item.ping,
                        download = item.download,
                        upload = item.upload,
                        timestamp = item.timestamp,
                        onOpen = {
                            // TODO: Open speed test screen.
                        }
                    )
                }
            }

            item(
                key = "BottomSpacing",
                span = StaggeredGridItemSpan.FullLine
            ) {
                Spacer(modifier = Modifier.height(28.dp + containerPaddingValues.calculateBottomPadding()))
            }
        }
    }

    PaymentScreen(
        modifier = Modifier.fillMaxWidth(),
        sheetState = paymentBottomSheetState,
        purchasingState = paymentPurchasingState
    )

    ServerDetailsScreen(
        modifier = Modifier.fillMaxWidth(),
        state = serverDetailsBottomSheetState
    )

    MoonShieldDescriptionBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        sheetState = moonShieldBottomSheetState
    )

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showError(notification = errorMessage)
        }
    }

    LaunchedEffect(viewModel.state.current.value.successMessage) {
        viewModel.state.current.value.successMessage?.let { successMessage ->
            snackbarHostState.showSuccess(notification = successMessage)
        }
    }
}
