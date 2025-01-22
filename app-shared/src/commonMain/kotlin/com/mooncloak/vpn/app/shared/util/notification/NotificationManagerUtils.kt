package com.mooncloak.vpn.app.shared.util.notification

public suspend fun NotificationManager.cancelVPNNotification() {
    this.cancel(notificationId = NotificationId.VPN)
}
