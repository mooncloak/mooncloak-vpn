package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheet
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.feature.collaborator.container.CollaboratorContainerScreen
import com.mooncloak.vpn.app.shared.feature.dependency.DependencyLicenseListScreen
import com.mooncloak.vpn.app.shared.feature.payment.purchase.PaymentScreen
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsBottomSheetDestination
import com.mooncloak.vpn.app.shared.feature.wireguard.dns.DnsServerConfigScreen
import kotlinx.coroutines.launch

@Composable
internal fun SettingsBottomSheet(
    state: ModalNavigationBottomSheetState<SettingsBottomSheetDestination>,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationBottomSheet(
        state = state,
        modifier = modifier
    ) { destination ->
        when (destination) {
            is SettingsBottomSheetDestination.SelectPlan -> PaymentScreen(
                modifier = Modifier.fillMaxWidth()
            )

            is SettingsBottomSheetDestination.Collaborators -> CollaboratorContainerScreen(
                modifier = Modifier.fillMaxWidth()
            )

            is SettingsBottomSheetDestination.AppInfo -> AppDetailsBottomSheetLayout(
                modifier = Modifier.fillMaxWidth(),
                details = destination.details
            )

            is SettingsBottomSheetDestination.DeviceInfo -> DeviceDetailsBottomSheetLayout(
                modifier = Modifier.fillMaxWidth(),
                details = destination.details
            )

            is SettingsBottomSheetDestination.DnsServerConfig -> DnsServerConfigScreen(
                modifier = Modifier.fillMaxWidth(),
                onSave = {
                    coroutineScope.launch {
                        state.hide()
                    }
                }
            )
        }
    }
}
