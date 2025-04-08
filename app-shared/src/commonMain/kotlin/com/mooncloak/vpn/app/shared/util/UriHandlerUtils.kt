package com.mooncloak.vpn.app.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.UriHandler

@Composable
public expect fun platformDefaultUriHandler(): UriHandler

public expect suspend fun UriHandler.openEmail(
    to: List<String>,
    subject: String?
)

public interface AppChooser : UriHandler {

    override fun openUri(uri: String) {
        openUri(uri = uri, message = "Choose app")
    }

    public fun openUri(uri: String, message: String)

    public companion object
}

public val LocalAppChooser: ProvidableCompositionLocal<AppChooser> =
    staticCompositionLocalOf { error("No AppChooser provided.") }

@Composable
public expect fun rememberPlatformDefaultAppChooser(): AppChooser
