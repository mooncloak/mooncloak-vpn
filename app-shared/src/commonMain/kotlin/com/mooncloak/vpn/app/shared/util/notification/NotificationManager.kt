package com.mooncloak.vpn.app.shared.util.notification

import androidx.compose.ui.graphics.Color

public interface NotificationManager {

    public suspend fun hasPermission(): Boolean

    public suspend fun requestPermission()

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
        actions: List<NotificationAction> = emptyList(),
        onlyAlertOnce: Boolean = true,
        ongoing: Boolean = false,
        color: Color? = null
    ): Boolean

    public suspend fun cancel(notificationId: Int)

    public suspend fun cancelAll()

    public object ChannelId {

        public const val VPN: String = "com.mooncloak.vpn.app.notification.channel.vpn"
    }

    public object NotificationId {

        public const val VPN: Int = 0
    }

    public companion object
}

public expect suspend fun NotificationManager.showVPNNotification()

public suspend fun NotificationManager.cancelVPNNotification() {
    this.cancel(notificationId = NotificationManager.NotificationId.VPN)
}
