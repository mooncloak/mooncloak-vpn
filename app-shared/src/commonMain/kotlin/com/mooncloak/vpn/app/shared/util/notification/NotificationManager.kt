package com.mooncloak.vpn.app.shared.util.notification

import androidx.compose.ui.graphics.Color

public expect interface NotificationManager {

    public suspend fun areEnabled(): Boolean

    public suspend fun registerNotificationChannel(
        id: String,
        name: String,
        description: String? = null,
        priority: NotificationPriority = NotificationPriority.DEFAULT
    )

    public suspend fun showNotification(
        channelId: String,
        notificationId: Int,
        title: String,
        body: String? = null,
        priority: NotificationPriority = NotificationPriority.DEFAULT,
        category: NotificationCategory? = null,
        lockScreenVisibility: NotificationLockScreenVisibility = NotificationLockScreenVisibility.Private,
        tapAction: NotificationAction? = null,
        actions: List<NotificationAction> = emptyList(),
        onlyAlertOnce: Boolean = true,
        ongoing: Boolean = false,
        color: Color? = null
    ): Boolean

    public suspend fun cancel(notificationId: Int)

    public suspend fun cancelAll()

    public companion object
}
