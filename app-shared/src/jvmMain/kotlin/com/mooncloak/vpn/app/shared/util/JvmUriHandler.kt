package com.mooncloak.vpn.app.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.UriHandler

@Composable
public actual fun platformDefaultUriHandler(): UriHandler = TODO()

public actual fun UriHandler.openEmail(
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
