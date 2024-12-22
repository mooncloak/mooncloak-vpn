package com.mooncloak.vpn.app.desktop.window

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.WindowState
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.app.ApplicationRootScreen
import com.mooncloak.vpn.app.shared.theme.ThemePreference

@Composable
internal fun MainWindow(
    applicationDependencies: ApplicationComponent,
    presentationDependencies: PresentationComponent,
    state: WindowState,
    onClose: () -> Unit,
    visible: Boolean = true
) {
    ThemedWindow(
        onCloseRequest = onClose,
        themePreference = ThemePreference.System,
        state = state,
        visible = visible
    ) {
        ApplicationRootScreen(
            applicationComponent = applicationDependencies,
            presentationComponent = presentationDependencies
        )
    }
}
