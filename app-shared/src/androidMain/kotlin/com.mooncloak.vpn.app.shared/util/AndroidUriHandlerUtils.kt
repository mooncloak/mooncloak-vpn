package com.mooncloak.vpn.app.shared.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
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

public actual fun UriHandler.openEmail(
    to: List<String>,
    subject: String?,
) {
    if (this !is AndroidCustomTabsHandler) {
        val uri = buildString {
            append("mailto:")

            if (to.isNotEmpty()) {
                append(to.joinToString(separator = ","))
            }

            if (!subject.isNullOrBlank()) {
                append("?subject=$subject")
            }
        }

        this.openUri(uri)
    } else {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            setData(Uri.parse("mailto:"))

            if (to.isNotEmpty()) {
                putExtra(Intent.EXTRA_EMAIL, to.toTypedArray())
            }

            if (!subject.isNullOrBlank()) {
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }
        }

        val activityStarted = try {
            emailIntent.resolveActivity(context.packageManager)

            true
        } catch (_: ActivityNotFoundException) {
            false
        }

        if (!activityStarted) {
            context.startActivity(Intent.createChooser(emailIntent, "Email"))
        }
    }
}

internal class AndroidCustomTabsHandler internal constructor(
    internal val context: Context,
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
