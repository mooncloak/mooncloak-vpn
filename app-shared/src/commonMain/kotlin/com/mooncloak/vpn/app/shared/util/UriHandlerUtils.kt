package com.mooncloak.vpn.app.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.UriHandler

@Composable
public expect fun platformDefaultUriHandler(): UriHandler

public expect fun UriHandler.openEmail(
    to: List<String>,
    subject: String?
)
