package com.mooncloak.vpn.app.shared.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.mooncloak.vpn.app.shared.feature.home.composable.ServerConnectionCard
import com.mooncloak.vpn.app.shared.feature.server.composable.AdShieldCard

@Composable
public fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = remember { HomeViewModel() }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        val connected = remember { mutableStateOf(false) }

        Column {
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
        }
    }

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { errorMessage ->
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }
}
