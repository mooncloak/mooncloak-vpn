package com.mooncloak.vpn.app.shared.util.notification

import android.app.Notification
import android.content.Context
import androidx.compose.ui.graphics.Color

public actual interface NotificationManager {

    public val context: Context

    public actual suspend fun areEnabled(): Boolean

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

    public suspend fun getNotification(
        channelId: String,
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
    ): Notification

    public actual suspend fun cancel(notificationId: Int)

    public actual suspend fun cancelAll()

    public actual companion object
}
