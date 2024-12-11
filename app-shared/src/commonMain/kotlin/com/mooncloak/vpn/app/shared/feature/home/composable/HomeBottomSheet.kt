package com.mooncloak.vpn.app.shared.feature.home.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheet
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.feature.home.model.HomeBottomSheetDestination
import com.mooncloak.vpn.app.shared.feature.payment.PaymentScreen
import com.mooncloak.vpn.app.shared.feature.server.ServerDetailsScreen

@Composable
internal fun HomeBottomSheet(
    state: ModalNavigationBottomSheetState,
    modifier: Modifier = Modifier
) {
    ModalNavigationBottomSheet(
        startDestination = HomeBottomSheetDestination.Empty,
        state = state,
        modifier = modifier
    ) {
        composable<HomeBottomSheetDestination.Empty> { }
        composable<HomeBottomSheetDestination.Payment> {
            PaymentScreen(modifier = Modifier.fillMaxSize())
        }
        composable<HomeBottomSheetDestination.ServerDetails> {
            ServerDetailsScreen(modifier = Modifier.fillMaxSize())
        }
    }
}
