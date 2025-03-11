package com.mooncloak.vpn.app.shared.util.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.notification_action_vpn_disconnect
import com.mooncloak.vpn.app.shared.resource.notification_title_shortcuts
import com.mooncloak.vpn.app.shared.resource.notification_title_vpn
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import com.mooncloak.vpn.util.notification.NotificationAction
import com.mooncloak.vpn.util.notification.NotificationCategory
import com.mooncloak.vpn.util.notification.NotificationChannelId
import com.mooncloak.vpn.util.notification.NotificationManager
import com.mooncloak.vpn.util.notification.NotificationPriority
import org.jetbrains.compose.resources.getString

public suspend fun NotificationManager.getVPNNotification(
    openAppIntent: Intent,
    disconnectIntent: Intent
): Notification = this.getNotification(
        channelId = NotificationChannelId.VPN,
        title = getString(Res.string.notification_title_vpn),
        priority = NotificationPriority.MAX,
        category = NotificationCategory.Service,
        color = ColorPalette.MooncloakDarkPrimary,
        ongoing = true,
        tapAction = NotificationAction(
            title = "",
            intent = PendingIntent.getActivity(
                context,
                0,
                openAppIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        ),
        actions = listOf(
            NotificationAction(
                title = getString(Res.string.notification_action_vpn_disconnect),
                intent = PendingIntent.getBroadcast(
                    context,
                    0,
                    disconnectIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
        )
    )

public suspend fun NotificationManager.getShortcutNotification(): Notification = this.getNotification(
    channelId = NotificationChannelId.SHORTCUTS,
    title = getString(Res.string.notification_title_shortcuts),
    priority = NotificationPriority.MAX,
    category = NotificationCategory.Service,
    color = ColorPalette.MooncloakDarkPrimary,
    ongoing = true
)
