package com.mooncloak.vpn.app.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.UriHandler

@Composable
public actual fun platformDefaultUriHandler(): UriHandler = TODO()

public actual suspend fun UriHandler.openEmail(
    to: List<String>,
    subject: String?,
) {
    TODO()
}
