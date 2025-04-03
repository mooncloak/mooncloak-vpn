package com.mooncloak.vpn.app.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString

@Composable
public actual fun rememberPlatformDefaultShareHandler(): ShareHandler {
    val clipboardManager = LocalClipboardManager.current

    return remember(clipboardManager) { JvmShareHandler(clipboardManager = clipboardManager) }
}

internal class JvmShareHandler internal constructor(
    private val clipboardManager: ClipboardManager
) : ShareHandler {

    override fun share(text: String, mimeType: String, message: String) {
        clipboardManager.setText(AnnotatedString(text))
    }
}
