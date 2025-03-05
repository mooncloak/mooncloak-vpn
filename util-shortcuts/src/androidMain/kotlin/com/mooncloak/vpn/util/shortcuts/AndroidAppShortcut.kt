package com.mooncloak.vpn.util.shortcuts

import android.content.Intent
import androidx.core.graphics.drawable.IconCompat

public actual data class AppShortcut public constructor(
    public actual val id: String,
    public actual val shortLabel: String,
    public actual val longLabel: String? = null,
    public val icon: IconCompat? = null,
    public val intent: Intent
)
