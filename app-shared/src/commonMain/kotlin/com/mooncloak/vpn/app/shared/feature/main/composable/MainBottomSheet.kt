package com.mooncloak.vpn.app.shared.feature.main.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheet
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.feature.main.model.MainBottomSheetDestination
import com.mooncloak.vpn.app.shared.feature.server.connection.ServerConnectionScreen

@Composable
internal fun MainBottomSheet(
    state: ModalNavigationBottomSheetState<MainBottomSheetDestination>,
    modifier: Modifier = Modifier
) {
    ModalNavigationBottomSheet(
        state = state,
        modifier = modifier
    ) { destination ->
        when (destination) {
            is MainBottomSheetDestination.ServerConnection -> ServerConnectionScreen(
                modifier = Modifier.fillMaxWidth(),
                server = destination.server
            )
        }
    }
}
