package com.mooncloak.vpn.app.desktop.window

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.app.ApplicationRootScreen
import com.mooncloak.vpn.app.shared.theme.ThemePreference
import com.mooncloak.vpn.app.shared.window.rememberWindowTitleBarState
import com.mooncloak.vpn.data.shared.keyvalue.state

@Composable
internal fun MainWindow(
    applicationDependencies: ApplicationComponent,
    presentationDependencies: PresentationComponent,
    state: WindowState = rememberWindowState(),
    onClose: () -> Unit,
    visible: Boolean = true
) {
    val windowTitleBarState = rememberWindowTitleBarState(
        title = applicationDependencies.appClientInfo.name,
    )

    val themePreference = applicationDependencies.preferenceStorage.theme.state(initial = ThemePreference.System)

    MooncloakDecorationWindow(
        onClose = onClose,
        themePreference = themePreference.value ?: ThemePreference.System,
        state = state,
        visible = visible,
        titleBarState = windowTitleBarState
    ) {
        ApplicationRootScreen(
            applicationComponent = applicationDependencies,
            presentationComponent = presentationDependencies
        )
    }
}
