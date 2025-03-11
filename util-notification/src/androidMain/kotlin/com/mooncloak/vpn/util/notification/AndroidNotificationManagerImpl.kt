package com.mooncloak.vpn.util.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

public operator fun com.mooncloak.vpn.util.notification.NotificationManager.Companion.invoke(context: Context): com.mooncloak.vpn.util.notification.NotificationManager =
    AndroidNotificationManagerImpl(context = context)

internal class AndroidNotificationManagerImpl internal constructor(
    override val context: Context
) : com.mooncloak.vpn.util.notification.NotificationManager {

    override suspend fun areEnabled(): Boolean =
        NotificationManagerCompat.from(context).areNotificationsEnabled()

    override suspend fun registerNotificationChannel(
        id: String,
        name: String,
        description: String?,
        priority: NotificationPriority
    ) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = when (priority) {
                NotificationPriority.DEFAULT -> NotificationManager.IMPORTANCE_DEFAULT
                NotificationPriority.MIN -> NotificationManager.IMPORTANCE_MIN
                NotificationPriority.LOW -> NotificationManager.IMPORTANCE_LOW
                NotificationPriority.HIGH -> NotificationManager.IMPORTANCE_HIGH
                NotificationPriority.MAX -> NotificationManager.IMPORTANCE_MAX
            }

            val channel = NotificationChannel(id, name, importance)
            channel.description = description

            // Register the channel with the system.
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
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
        val notification = getNotification(
            channelId = channelId,
            title = title,
            body = body,
            priority = priority,
            category = category,
            lockScreenVisibility = lockScreenVisibility,
            tapAction = tapAction,
            actions = actions,
            onlyAlertOnce = onlyAlertOnce,
            ongoing = ongoing,
            color = color
        )

        val manager = NotificationManagerCompat.from(this.context)

        if (areEnabled()) {
            manager.notify(notificationId, notification)

            return true
        } else {

            return false
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun getNotification(
        channelId: String,
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
    ): Notification {
        var builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(priority.toAndroidPriority())
            .setAllowSystemGeneratedContextualActions(false)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setVisibility(lockScreenVisibility.toAndroidVisibility())
            .setOnlyAlertOnce(onlyAlertOnce)
            .setOngoing(ongoing)

        if (category != null) {
            builder = builder.setCategory(category.toAndroidCategory())
        }

        if (color != null) {
            builder = builder.setColor(color.toArgb())
        }

        if (tapAction != null) {
            builder = builder.setContentIntent(tapAction.intent)
        }

        for (action in actions) {
            builder = builder.addAction(
                action.iconRes ?: 0,
                action.title,
                action.intent
            )
        }

        return builder.build()
    }

    override suspend fun cancel(notificationId: Int) {
        val manager = NotificationManagerCompat.from(context)

        manager.cancel(notificationId)
    }

    override suspend fun cancelAll() {
        val manager = NotificationManagerCompat.from(context)

        manager.cancelAll()
    }

    internal companion object {

        private const val TAG: String = "AndroidNotificationManager"
    }
}
