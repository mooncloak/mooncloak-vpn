package com.mooncloak.vpn.app.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.UriHandler
import io.ktor.http.encodeURLQueryComponent
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Composable
public actual fun platformDefaultUriHandler(): UriHandler = remember { IosUriHandler() }

@Composable
public actual fun rememberPlatformDefaultAppChooser(): AppChooser =
    remember { IosAppChooser() }

public actual suspend fun UriHandler.openEmail(
    to: List<String>,
    subject: String?,
) {
    val toParam = to.joinToString(",").encodeURLQueryComponent()
    val subjectParam = subject?.encodeURLQueryComponent() ?: ""
    val mailtoUrl = "mailto:$toParam?subject=$subjectParam"

    suspendCancellableCoroutine { continuation ->
        val url = NSURL.URLWithString(mailtoUrl)
        if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(
                url = url,
                options = emptyMap<Any?, Any?>(),
                completionHandler = { success ->
                    if (success) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(Exception("Failed to open email client"))
                    }
                })
        } else {
            continuation.resumeWithException(Exception("No email client available or invalid URL"))
        }
    }
}

internal class IosUriHandler internal constructor() : UriHandler {

    override fun openUri(uri: String) {
        val url = NSURL.URLWithString(uri)

        if (url != null) {
            UIApplication.sharedApplication.openURL(url)
        }
    }
}

internal class IosAppChooser internal constructor() : AppChooser {

    override fun openUri(uri: String, message: String) {
        val url = NSURL.URLWithString(uri)

        if (url != null) {
            val activityController = UIActivityViewController(
                activityItems = listOf(url),
                applicationActivities = null
            )

            val rootController = UIApplication.sharedApplication.keyWindow?.rootViewController

            rootController?.presentViewController(activityController, animated = true, completion = null)
        }
    }
}
