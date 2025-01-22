package com.mooncloak.vpn.app.shared.util.notification

import androidx.compose.ui.graphics.Color

public actual interface NotificationManager {

    public actual suspend fun areEnabled(): Boolean

    public actual suspend fun requestPermission()

    public actual suspend fun registerNotificationChannel(
        id: String,
        name: String,
        description: String?,
        priority: NotificationPriority
    )

    public actual suspend fun showNotification(
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
    ): Boolean

    public actual suspend fun cancel(notificationId: Int)

    public actual suspend fun cancelAll()

    public actual companion object
}
