package com.mooncloak.vpn.app.shared.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.api.ConnectionType
import com.mooncloak.vpn.app.shared.feature.home.composable.HomeTitleBar
import com.mooncloak.vpn.app.shared.feature.home.composable.PlanUsageCard
import com.mooncloak.vpn.app.shared.feature.home.composable.ServerConnectionCard
import com.mooncloak.vpn.app.shared.feature.home.composable.AdShieldCard
import com.mooncloak.vpn.app.shared.feature.home.composable.GetVPNServiceCard
import com.mooncloak.vpn.app.shared.feature.home.composable.HomeTitleBarConnectionStatus
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

@Composable
public fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = remember { HomeViewModel() }
    val snackbarHostState = remember { SnackbarHostState() }

    val status = remember { mutableStateOf(HomeTitleBarConnectionStatus.Disconnected) }

    LaunchedEffect(Unit) {
        viewModel.load()

        delay(2.seconds)

        status.value = HomeTitleBarConnectionStatus.Connecting

        delay(2.seconds)

        status.value = HomeTitleBarConnectionStatus.Connected
    }

    // TODOS:
    // * Showcase cards for when the user has not signed-up for service
    // * "Get Protected" card for when the user has not signed-up for service
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

        Column(
            modifier = Modifier.padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            GetVPNServiceCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                onClick = {}
            )

            ServerConnectionCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
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

            AdShieldCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                adsBlocked = 0,
                trackersBlocked = 0,
                bytesSaved = 0L,
                active = true,
                onClick = {

                }
            )

            PlanUsageCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                durationRemaining = 30.days,
                bytesRemaining = 1000,
                boost = true,
                onBoost = {

                }
            )
        }
    }

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
