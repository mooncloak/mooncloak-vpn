package com.mooncloak.vpn.app.shared.util

import androidx.compose.ui.text.TextStyle

public actual fun TextStyle.withoutFontPadding(): TextStyle =
    this.copy()
