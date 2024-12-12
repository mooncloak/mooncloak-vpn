package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.composable
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheet
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.feature.dependency.DependencyLicenseListScreen
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsBottomSheetDestination
import com.mooncloak.vpn.app.shared.feature.subscription.SubscriptionScreen

@Composable
internal fun SettingsBottomSheet(
    state: ModalNavigationBottomSheetState,
    modifier: Modifier = Modifier
) {
    ModalNavigationBottomSheet(
        startDestination = SettingsBottomSheetDestination.Empty,
        state = state,
        modifier = modifier
    ) {
        composable<SettingsBottomSheetDestination.Empty> {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(400.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        composable<SettingsBottomSheetDestination.DependencyLicenseList> {
            DependencyLicenseListScreen(modifier = Modifier.fillMaxSize())
        }
        composable<SettingsBottomSheetDestination.Subscription> {
            SubscriptionScreen(modifier = Modifier.fillMaxSize())
        }
    }
}
