package com.mooncloak.vpn.app.shared.util

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler

@Composable
public actual fun platformDefaultUriHandler(): UriHandler {
    val context = LocalContext.current
    val defaultUriHandler = LocalUriHandler.current

    return remember(context, defaultUriHandler) {
        AndroidCustomTabsHandler(
            context = context,
            defaultUriHandler = defaultUriHandler
        )
    }
}

internal class AndroidCustomTabsHandler internal constructor(
    private val context: Context,
    private val defaultUriHandler: UriHandler?
) : UriHandler {

    override fun openUri(uri: String) {
        if (defaultUriHandler != null && !uri.startsWith("http")) {
            defaultUriHandler.openUri(uri)
        } else {
            CustomTabsIntent.Builder()
                .build()
                .launchUrl(context, Uri.parse(uri))
        }
    }
}
