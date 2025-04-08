package com.mooncloak.vpn.app.shared.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.support_email_action
import org.jetbrains.compose.resources.getString
import androidx.core.net.toUri

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

@Composable
public actual fun rememberPlatformDefaultAppChooser(): AppChooser {
    val context = LocalContext.current

    return remember(context) {
        AndroidAppChooser(context = context)
    }
}

public actual suspend fun UriHandler.openEmail(
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
            data = Uri.parse("mailto:")

            if (to.isNotEmpty()) {
                putExtra(Intent.EXTRA_EMAIL, to.toTypedArray())
            }

            if (!subject.isNullOrBlank()) {
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }
        }

        // For some unknown reason, the Email intent for the default Email app stopped working. It wouldn't do anything
        // but would claim that the activity "started" (when wrapped in a try/catch for ActivityNotFound). To solve
        // this, just always show the Intent chooser.
        context.startActivity(Intent.createChooser(emailIntent, getString(Res.string.support_email_action)))
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

internal class AndroidAppChooser internal constructor(
    private val context: Context
) : AppChooser {

    override fun openUri(uri: String, message: String) {
        val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
        val chooser = Intent.createChooser(intent, message)
        context.startActivity(chooser)
    }
}
