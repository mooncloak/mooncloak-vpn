package com.mooncloak.vpn.app.shared.feature.home.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheet
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.feature.home.model.HomeBottomSheetDestination
import com.mooncloak.vpn.app.shared.feature.payment.purchase.PaymentScreen
import com.mooncloak.vpn.app.shared.feature.server.details.ServerDetailsScreen

@Composable
internal fun HomeBottomSheet(
    state: ModalNavigationBottomSheetState<HomeBottomSheetDestination>,
    modifier: Modifier = Modifier
) {
    ModalNavigationBottomSheet(
        state = state,
        modifier = modifier
    ) { destination ->
        when (destination) {
            is HomeBottomSheetDestination.Payment -> PaymentScreen(modifier = Modifier.fillMaxSize())
            is HomeBottomSheetDestination.ServerDetails -> ServerDetailsScreen(modifier = Modifier.fillMaxSize())
        }
    }
}
