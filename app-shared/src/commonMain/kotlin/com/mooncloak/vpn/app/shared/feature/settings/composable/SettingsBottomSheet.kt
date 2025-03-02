package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheet
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.feature.collaborator.container.CollaboratorContainerScreen
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsBottomSheetDestination

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
            is SettingsBottomSheetDestination.Collaborators -> CollaboratorContainerScreen(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
