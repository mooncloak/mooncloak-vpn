package com.mooncloak.vpn.app.android.service

import android.content.Intent
import com.mooncloak.vpn.app.android.activity.MainActivity
import com.mooncloak.vpn.app.android.di.applicationDependency
import com.mooncloak.vpn.app.android.receiver.DisconnectTunnelsBroadcastReceiver
import com.mooncloak.vpn.app.shared.util.notification.NotificationId
import com.mooncloak.vpn.app.shared.util.notification.NotificationManager
import com.mooncloak.vpn.app.shared.util.notification.getVPNNotification
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.backend.GoBackend.VpnService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * The mooncloak VPN Android service. This component extends the WireGuard [GoBackend.VpnService] and provides
 * additional functionality, such as setting the service as a foreground service.
 */
public class MooncloakVpnService : VpnService() {

    private val coroutineScope: CoroutineScope = MainScope()

    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        notificationManager = this.applicationDependency { this.notificationManager }

        super.onCreate()

        coroutineScope.launch {
            val notification = notificationManager.getVPNNotification(
                openAppIntent = Intent(this@MooncloakVpnService, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                },
                disconnectIntent = Intent(
                    this@MooncloakVpnService,
                    DisconnectTunnelsBroadcastReceiver::class.java
                ).apply {
                    action = DisconnectTunnelsBroadcastReceiver.ACTION
                }
            )

            this@MooncloakVpnService.startForeground(NotificationId.VPN, notification)
        }
    }

    override fun onDestroy() {
        coroutineScope.launch {
            notificationManager.cancel(NotificationId.VPN)
        }

        super.onDestroy()
    }
}
