package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheet
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.feature.collaborator.container.CollaboratorContainerScreen
import com.mooncloak.vpn.app.shared.feature.dependency.DependencyLicenseListScreen
import com.mooncloak.vpn.app.shared.feature.payment.history.PaymentHistoryScreen
import com.mooncloak.vpn.app.shared.feature.payment.purchase.PaymentScreen
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsBottomSheetDestination
import com.mooncloak.vpn.app.shared.feature.subscription.SubscriptionScreen
import com.mooncloak.vpn.app.shared.feature.wireguard.dns.DnsServerConfigScreen

@Composable
internal fun SettingsBottomSheet(
    state: ModalNavigationBottomSheetState<SettingsBottomSheetDestination>,
    onOpenPlans: () -> Unit,
    onOpenTransactionHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalNavigationBottomSheet(
        state = state,
        modifier = modifier
    ) { destination ->
        when (destination) {
            is SettingsBottomSheetDestination.DependencyLicenseList -> DependencyLicenseListScreen(
                modifier = Modifier.fillMaxWidth()
            )

            is SettingsBottomSheetDestination.Subscription -> SubscriptionScreen(
                modifier = Modifier.fillMaxWidth(),
                onOpenPlans = onOpenPlans,
                onOpenPaymentHistory = onOpenTransactionHistory
            )

            is SettingsBottomSheetDestination.TransactionHistory -> PaymentHistoryScreen(
                modifier = Modifier.fillMaxWidth(),
                onGetService = onOpenPlans
            )

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
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
