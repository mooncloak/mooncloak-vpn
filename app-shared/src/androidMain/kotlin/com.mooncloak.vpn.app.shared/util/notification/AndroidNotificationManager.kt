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
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.notification_title_vpn
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getString
import kotlin.coroutines.resume

public actual suspend fun com.mooncloak.vpn.app.shared.util.notification.NotificationManager.showVPNNotification() {
    this.showNotification(
        channelId = com.mooncloak.vpn.app.shared.util.notification.NotificationManager.ChannelId.VPN,
        notificationId = com.mooncloak.vpn.app.shared.util.notification.NotificationManager.NotificationId.VPN,
        title = getString(Res.string.notification_title_vpn),
        priority = NotificationPriority.MAX,
        category = NotificationCategory.Service,
        color = ColorPalette.MooncloakDarkPrimary
    )
}

public class AndroidNotificationManager @Inject public constructor(
    private val activity: Activity
) : com.mooncloak.vpn.app.shared.util.notification.NotificationManager {

    private var intentCallback: (() -> Unit)? = null

    override suspend fun hasPermission(): Boolean =
        NotificationManagerCompat.from(activity).areNotificationsEnabled()

    override suspend fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                activity,
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
                activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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
        actions: List<NotificationAction>,
        onlyAlertOnce: Boolean,
        ongoing: Boolean,
        color: Color?
    ): Boolean {
        var builder = NotificationCompat.Builder(activity, channelId)
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

        for (action in actions) {
            builder = builder.addAction(
                action.iconRes ?: 0,
                action.title,
                action.intent
            )
        }

        val notification = builder.build()

        val manager = NotificationManagerCompat.from(activity)

        if (hasPermission()) {
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

            if (hasPermission()) {
                manager.notify(notificationId, notification)

                return true
            }

            return false
        }
    }

    override suspend fun cancel(notificationId: Int) {
        val manager = NotificationManagerCompat.from(activity)

        manager.cancel(notificationId)
    }

    override suspend fun cancelAll() {
        val manager = NotificationManagerCompat.from(activity)

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
