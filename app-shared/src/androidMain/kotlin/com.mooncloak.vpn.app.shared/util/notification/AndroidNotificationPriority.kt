package com.mooncloak.vpn.app.shared.util.notification

import androidx.core.app.NotificationCompat

internal fun NotificationPriority.toAndroidPriority(): Int =
    when (this) {
        NotificationPriority.DEFAULT -> NotificationCompat.PRIORITY_DEFAULT
        NotificationPriority.MIN -> NotificationCompat.PRIORITY_MIN
        NotificationPriority.LOW -> NotificationCompat.PRIORITY_LOW
        NotificationPriority.HIGH -> NotificationCompat.PRIORITY_HIGH
        NotificationPriority.MAX -> NotificationCompat.PRIORITY_MAX
    }
