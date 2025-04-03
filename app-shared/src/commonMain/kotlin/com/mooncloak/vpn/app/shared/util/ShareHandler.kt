package com.mooncloak.vpn.app.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

public interface ShareHandler {

    public fun share(
        text: String,
        mimeType: String = "text/plain",
        message: String = "Share"
    )

    public companion object
}

public val LocalShareHandler: ProvidableCompositionLocal<ShareHandler> =
    staticCompositionLocalOf { error("No ShareHandler provided.") }

@Composable
public expect fun rememberPlatformDefaultShareHandler(): ShareHandler
