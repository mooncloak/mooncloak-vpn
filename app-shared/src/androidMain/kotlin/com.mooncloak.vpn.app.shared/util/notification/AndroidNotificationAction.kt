package com.mooncloak.vpn.app.shared.util.notification

import android.app.PendingIntent
import androidx.compose.runtime.Immutable

@Immutable
public actual class NotificationAction public constructor(
    public actual val title: String,
    public val iconRes: Int? = null,
    public val intent: PendingIntent
)
