package com.mooncloak.vpn.util.notification

import androidx.core.app.NotificationCompat

internal fun NotificationLockScreenVisibility.toAndroidVisibility(): Int =
    when (this) {
        NotificationLockScreenVisibility.Public -> NotificationCompat.VISIBILITY_PUBLIC
        NotificationLockScreenVisibility.Secret -> NotificationCompat.VISIBILITY_SECRET
        NotificationLockScreenVisibility.Private -> NotificationCompat.VISIBILITY_PRIVATE
    }
