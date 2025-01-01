package com.mooncloak.vpn.app.shared.feature.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.rememberModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.home.composable.HomeTitleBar
import com.mooncloak.vpn.app.shared.feature.home.composable.PlanUsageCard
import com.mooncloak.vpn.app.shared.feature.home.composable.ServerConnectionCard
import com.mooncloak.vpn.app.shared.feature.home.composable.AdShieldCard
import com.mooncloak.vpn.app.shared.feature.home.composable.GetVPNServiceCard
import com.mooncloak.vpn.app.shared.feature.home.composable.HomeBottomSheet
import com.mooncloak.vpn.app.shared.feature.home.composable.ShowcaseCard
import com.mooncloak.vpn.app.shared.feature.home.di.createHomeComponent
import com.mooncloak.vpn.app.shared.feature.home.model.HomeBottomSheetDestination
import com.mooncloak.vpn.app.shared.feature.home.model.HomeFeedItem
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
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
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalNavigationBottomSheetState<HomeBottomSheetDestination>()
    val hazeState = remember { HazeState() }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    println("HomeScreen: state: ${viewModel.state.current.value}")

    // TODOS:
    // * Recently used VPN service card
    // * Starred VPN service card

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            HomeTitleBar(
                modifier = Modifier.animateContentSize()
                    .fillMaxWidth()
                    .shadow(elevation = 8.dp),
                status = viewModel.state.current.value.connectionStatus,
                connectedName = viewModel.state.current.value.connectedName,
                ipAddress = viewModel.state.current.value.connectedIpAddress,
                hazeState = hazeState
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .haze(state = hazeState)
                .padding(horizontal = 12.dp),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "TopSpacing") {
                Spacer(modifier = Modifier.height(paddingValues.calculateTopPadding() + containerPaddingValues.calculateTopPadding()))
            }

            items(
                items = viewModel.state.current.value.items,
                key = { item -> item.toString() },
                contentType = { "HomeFeedItem" }
            ) { item ->
                when (item) {
                    is HomeFeedItem.AdShieldItem -> AdShieldCard(
                        modifier = Modifier.fillMaxWidth(),
                        adsBlocked = item.adsBlocked ?: 0,
                        trackersBlocked = item.trackersBlocked ?: 0,
                        bytesSaved = item.estimatedBytesSaved,
                        active = item.active,
                        onClick = {
                            // TODO: Open AdShield Details
                        }
                    )

                    HomeFeedItem.GetVPNServiceItem -> GetVPNServiceCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            coroutineScope.launch {
                                bottomSheetState.show(destination = HomeBottomSheetDestination.Payment)
                            }
                        }
                    )

                    is HomeFeedItem.PlanUsageItem -> PlanUsageCard(
                        modifier = Modifier.fillMaxWidth(),
                        durationRemaining = item.durationRemaining,
                        bytesRemaining = item.bytesRemaining,
                        boost = item.showBoost,
                        onBoost = {
                            // TODO: Open Boost Flow
                        }
                    )

                    is HomeFeedItem.ServerConnectionItem -> ServerConnectionCard(
                        modifier = Modifier.fillMaxWidth(),
                        countryName = item.country.name,
                        countryFlag = item.country.flag,
                        serverName = item.server.name,
                        connectionType = item.connectionType,
                        connected = item.connected,
                        onConnect = {
                            viewModel.toggleConnection(server = item.server)
                        },
                        onDetails = {
                            // TODO: Open Server Details Screen
                            coroutineScope.launch {
                                bottomSheetState.show(HomeBottomSheetDestination.ServerDetails(item.server))
                            }
                        }
                    )

                    is HomeFeedItem.ShowcaseItem -> ShowcaseCard(
                        modifier = Modifier.fillMaxWidth(),
                        icon = item.icon.invoke(),
                        title = item.title.invoke(),
                        description = item.description.invoke()
                    )
                }
            }

            item(key = "BottomSpacing") {
                Spacer(modifier = Modifier.height(28.dp + containerPaddingValues.calculateBottomPadding()))
            }
        }
    }

    HomeBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        state = bottomSheetState
    )

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
