package com.mooncloak.vpn.app.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.warning
import java.awt.Desktop
import java.net.URI

internal class DesktopUriHandler internal constructor(
    private val desktop: Desktop? = if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop()
    } else {
        null
    }
) : UriHandler {

    override fun openUri(uri: String) {
        if (desktop?.isSupported(Desktop.Action.BROWSE) == true) {
            desktop.browse(URI.create(uri))
        } else {
            LogPile.warning(
                message = "Cannot open URI '$uri' as desktop environment does not support opening links."
            )
        }
    }
}

@Composable
public actual fun platformDefaultUriHandler(): UriHandler = DesktopUriHandler()

public actual suspend fun UriHandler.openEmail(
    to: List<String>,
    subject: String?,
) {
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
}
