package com.mooncloak.vpn.app.shared.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.api.ConnectionType
import com.mooncloak.vpn.app.shared.feature.home.composable.HomeTitleBar
import com.mooncloak.vpn.app.shared.feature.home.composable.PlanUsageCard
import com.mooncloak.vpn.app.shared.feature.home.composable.ServerConnectionCard
import com.mooncloak.vpn.app.shared.feature.home.composable.AdShieldCard
import com.mooncloak.vpn.app.shared.feature.home.composable.GetVPNServiceCard
import com.mooncloak.vpn.app.shared.feature.home.composable.HomeTitleBarConnectionStatus
import com.mooncloak.vpn.app.shared.feature.home.composable.ShowcaseCard
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.onboarding_description_no_tracking
import com.mooncloak.vpn.app.shared.resource.onboarding_title_no_tracking
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

@Composable
public fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = remember { HomeViewModel() }
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()

    val status = remember { mutableStateOf(HomeTitleBarConnectionStatus.Disconnected) }

    LaunchedEffect(Unit) {
        viewModel.load()

        delay(2.seconds)

        status.value = HomeTitleBarConnectionStatus.Connecting

        delay(2.seconds)

        status.value = HomeTitleBarConnectionStatus.Connected
    }

    // TODOS:
    // * Recently used VPN service card
    // * Starred VPN service card
    // * Format Data (Mb/Gb/etc.)

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            HomeTitleBar(
                modifier = Modifier.fillMaxWidth(),
                status = status.value,
                countryName = "United States",
                ipAddress = "192.168.99.1"
            )
        }
    ) { paddingValues ->
        val connected = remember { mutableStateOf(false) }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp),
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                GetVPNServiceCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {}
                )
            }

            item {
                ShowcaseCard(
                    modifier = Modifier.fillMaxWidth(),
                    icon = rememberVectorPainter(Icons.Default.CloudOff),
                    title = stringResource(Res.string.onboarding_title_no_tracking),
                    description = stringResource(Res.string.onboarding_description_no_tracking)
                )
            }

            item {
                ServerConnectionCard(
                    modifier = Modifier.fillMaxWidth(),
                    countryName = "United States",
                    countryFlag = null,
                    serverName = "Florida #12",
                    connectionType = ConnectionType.P2P,
                    connected = connected.value,
                    onConnect = {
                        connected.value = !connected.value
                    },
                    onDetails = {

                    }
                )
            }

            item {
                AdShieldCard(
                    modifier = Modifier.fillMaxWidth(),
                    adsBlocked = 0,
                    trackersBlocked = 0,
                    bytesSaved = 0L,
                    active = true,
                    onClick = {

                    }
                )
            }

            item {
                PlanUsageCard(
                    modifier = Modifier.fillMaxWidth(),
                    durationRemaining = 30.days,
                    bytesRemaining = 1000,
                    boost = true,
                    onBoost = {

                    }
                )
            }
        }
    }

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
