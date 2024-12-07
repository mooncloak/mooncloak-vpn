package com.mooncloak.vpn.app.desktop.window

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.WindowState

import com.mooncloak.vpn.app.desktop.di.JvmApplicationComponent
import com.mooncloak.vpn.app.shared.app.Application
import com.mooncloak.vpn.app.shared.theme.ThemePreference

@Composable
internal fun MainWindow(
    applicationDependencies: JvmApplicationComponent,
    state: WindowState,
    onClose: () -> Unit,
    visible: Boolean = true
) {
    ThemedWindow(
        onCloseRequest = onClose,
        themePreference = ThemePreference.SystemDefault,
        state = state,
        visible = visible
    ) {
        Application(
            component = applicationDependencies,
            builder = {

            }
        )
    }
}
