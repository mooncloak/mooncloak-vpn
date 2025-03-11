package com.mooncloak.vpn.util.notification

public suspend fun NotificationManager.cancelVPNNotification() {
    this.cancel(notificationId = NotificationId.VPN)
}
