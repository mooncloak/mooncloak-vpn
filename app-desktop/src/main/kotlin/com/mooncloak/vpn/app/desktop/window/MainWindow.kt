package com.mooncloak.vpn.app.desktop.window

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.WindowState
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.app.ApplicationRootScreen
import com.mooncloak.vpn.app.shared.theme.ThemePreference

@OptIn(ExperimentalPersistentStateAPI::class)
@Composable
internal fun MainWindow(
    applicationDependencies: ApplicationComponent,
    presentationDependencies: PresentationComponent,
    state: WindowState,
    onClose: () -> Unit,
    visible: Boolean = true
) {
    MooncloakDecorationWindow(
        onClose = onClose,
        themePreference = applicationDependencies.keyValueStorage.preferences.theme.current.value
            ?: ThemePreference.System,
        state = state,
        visible = visible
    ) {
        ApplicationRootScreen(
            applicationComponent = applicationDependencies,
            presentationComponent = presentationDependencies
        )
    }
}
