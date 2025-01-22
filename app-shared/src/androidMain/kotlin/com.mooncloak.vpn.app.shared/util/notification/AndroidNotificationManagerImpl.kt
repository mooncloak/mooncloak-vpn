package com.mooncloak.vpn.app.shared.util.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.info
import com.mooncloak.vpn.app.shared.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

public class AndroidNotificationManagerImpl @Inject public constructor(
    override val context: Activity
) : com.mooncloak.vpn.app.shared.util.notification.NotificationManager {

    private var intentCallback: (() -> Unit)? = null

    override suspend fun areEnabled(): Boolean =
        NotificationManagerCompat.from(context).areNotificationsEnabled()

    override suspend fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
    }

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

        val notification = builder.build()

        val manager = NotificationManagerCompat.from(this.context)

        if (areEnabled()) {
            manager.notify(notificationId, notification)

            return true
        } else {
            requestPermission()

            withContext(Dispatchers.IO) {
                suspendCancellableCoroutine { continuation ->
                    intentCallback = {
                        LogPile.info(tag = TAG, message = "Intent Callback.")

                        continuation.resume(Unit)

                        intentCallback = null
                    }
                }
            }

            if (areEnabled()) {
                manager.notify(notificationId, notification)

                return true
            }

            return false
        }
    }

    override suspend fun cancel(notificationId: Int) {
        val manager = NotificationManagerCompat.from(context)

        manager.cancel(notificationId)
    }

    override suspend fun cancelAll() {
        val manager = NotificationManagerCompat.from(context)

        manager.cancelAll()
    }

    public fun receivedResult(
        requestCode: Int,
        resultCode: Int
    ) {
        if (requestCode == REQUEST_CODE) {
            // TODO: Verify success/error
            intentCallback?.invoke()
        }
    }

    internal companion object {

        private const val TAG: String = "AndroidNotificationManager"
        private const val REQUEST_CODE: Int = 5678
    }
}
