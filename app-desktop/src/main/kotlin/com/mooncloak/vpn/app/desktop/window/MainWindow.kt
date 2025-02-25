package com.mooncloak.vpn.app.desktop.window

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.app.ApplicationRootScreen
import com.mooncloak.vpn.app.shared.theme.ThemePreference
import com.mooncloak.vpn.app.shared.window.rememberWindowTitleBarState

@OptIn(ExperimentalPersistentStateAPI::class)
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

    MooncloakDecorationWindow(
        onClose = onClose,
        themePreference = applicationDependencies.preferenceStorage.theme.current.value
            ?: ThemePreference.System,
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
