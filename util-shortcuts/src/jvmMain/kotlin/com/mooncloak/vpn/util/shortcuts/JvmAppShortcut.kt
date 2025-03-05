package com.mooncloak.vpn.util.shortcuts

import androidx.compose.ui.graphics.ImageBitmap

public actual data class AppShortcut public constructor(
    public actual val id: String,
    public actual val shortLabel: String,
    public actual val longLabel: String? = null,
    public val icon: ImageBitmap? = null
)
