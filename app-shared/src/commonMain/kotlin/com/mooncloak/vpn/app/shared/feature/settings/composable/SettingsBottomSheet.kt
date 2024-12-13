package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheet
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.feature.dependency.DependencyLicenseListScreen
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsBottomSheetDestination
import com.mooncloak.vpn.app.shared.feature.subscription.SubscriptionScreen

@Composable
internal fun SettingsBottomSheet(
    state: ModalNavigationBottomSheetState<SettingsBottomSheetDestination>,
    modifier: Modifier = Modifier
) {
    ModalNavigationBottomSheet(
        state = state,
        modifier = modifier
    ) { destination ->
        when (destination) {
            SettingsBottomSheetDestination.DependencyLicenseList -> DependencyLicenseListScreen(modifier = Modifier.fillMaxSize())
            SettingsBottomSheetDestination.Subscription -> SubscriptionScreen(modifier = Modifier.fillMaxSize())
        }
    }
}
