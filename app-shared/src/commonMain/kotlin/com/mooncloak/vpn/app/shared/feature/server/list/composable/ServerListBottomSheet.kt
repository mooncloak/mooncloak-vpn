package com.mooncloak.vpn.app.shared.feature.server.list.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheet
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.feature.payment.purchase.PaymentScreen
import com.mooncloak.vpn.app.shared.feature.server.connection.ServerConnectionScreen
import com.mooncloak.vpn.app.shared.feature.server.details.ServerDetailsScreen
import com.mooncloak.vpn.app.shared.feature.server.list.model.ServerListBottomSheetDestination

@Composable
internal fun ServerListBottomSheet(
    state: ModalNavigationBottomSheetState<ServerListBottomSheetDestination>,
    modifier: Modifier = Modifier
) {
    ModalNavigationBottomSheet(
        state = state,
        modifier = modifier
    ) { destination ->
        when (destination) {
            is ServerListBottomSheetDestination.Payment -> PaymentScreen(modifier = Modifier.fillMaxSize())

            is ServerListBottomSheetDestination.ServerDetails -> ServerDetailsScreen(
                modifier = Modifier.fillMaxSize(),
                server = destination.server
            )

            is ServerListBottomSheetDestination.ServerConnection -> ServerConnectionScreen(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
