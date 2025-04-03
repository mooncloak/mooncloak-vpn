package com.mooncloak.vpn.app.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

@Composable
public actual fun rememberPlatformDefaultShareHandler(): ShareHandler =
    remember { IosShareHandler() }

internal class IosShareHandler internal constructor() : ShareHandler {

    override fun share(text: String, mimeType: String, message: String) {
        val activityItems = listOf(text) // Can add more items like URLs, images
        val activityController = UIActivityViewController(activityItems, null)

        val rootController = UIApplication.sharedApplication.keyWindow?.rootViewController

        rootController?.presentViewController(activityController, true, null)
    }
}
