package com.mooncloak.vpn.util.notification

import androidx.compose.ui.graphics.Color

public operator fun NotificationManager.Companion.invoke(): NotificationManager =
    JvmNotificationImpl()

internal class JvmNotificationImpl internal constructor() : NotificationManager {

    override suspend fun areEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun registerNotificationChannel(
        id: String,
        name: String,
        description: String?,
        priority: NotificationPriority
    ) {
    }

    override suspend fun showNotification(
        channelId: String,
        notificationId: Int,
        title: String,
        body: String?,
        priority: NotificationPriority,
        category: NotificationCategory?,
        lockScreenVisibility: NotificationLockScreenVisibility,
        tapAction: NotificationAction?,
        actions: List<NotificationAction>,
        onlyAlertOnce: Boolean,
        ongoing: Boolean,
        color: Color?
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun cancel(notificationId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun cancelAll() {
        TODO("Not yet implemented")
    }
}
