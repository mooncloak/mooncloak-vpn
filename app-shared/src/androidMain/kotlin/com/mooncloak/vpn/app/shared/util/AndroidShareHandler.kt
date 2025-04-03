package com.mooncloak.vpn.app.shared.util

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
public actual fun rememberPlatformDefaultShareHandler(): ShareHandler {
    val context = LocalContext.current

    return remember(context) { AndroidShareHandler(context = context) }
}

internal class AndroidShareHandler internal constructor(
    private val context: Context
) : ShareHandler {

    override fun share(text: String, mimeType: String, message: String) {
        val intent = Intent(Intent.ACTION_SEND)
            .setType(mimeType)
            .putExtra(Intent.EXTRA_TEXT, text)

        val chooserIntent = Intent.createChooser(intent, message)

        context.startActivity(chooserIntent)
    }
}
