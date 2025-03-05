package com.mooncloak.vpn.app.android.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.tunnel.TunnelManager
import com.mooncloak.vpn.api.shared.util.launchActivity
import com.mooncloak.vpn.app.android.activity.MainActivity
import com.mooncloak.vpn.app.android.di.applicationDependency
import com.mooncloak.vpn.app.shared.api.server.usecase.GetDefaultServerUseCase
import com.mooncloak.vpn.app.shared.util.notification.NotificationId
import com.mooncloak.vpn.app.shared.util.notification.NotificationManager
import com.mooncloak.vpn.app.shared.util.notification.getShortcutNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public class QuickConnectService public constructor() : Service() {

    private val coroutineScope: CoroutineScope = MainScope()

    private val mutex = Mutex(locked = false)

    private lateinit var notificationManager: NotificationManager
    private lateinit var tunnelManager: TunnelManager
    private lateinit var getDefaultServer: GetDefaultServerUseCase

    override fun onCreate() {
        notificationManager = this.applicationDependency { notificationManager }
        tunnelManager = this.applicationDependency { tunnelManager }
        getDefaultServer = this.applicationDependency { getDefaultServer }

        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        coroutineScope.launch {
            val notification = notificationManager.getShortcutNotification()

            this@QuickConnectService.startForeground(NotificationId.SHORTCUT_ACTION, notification)

            toggleVPN()

            notificationManager.cancel(NotificationId.SHORTCUT_ACTION)

            stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        coroutineScope.launch {
            notificationManager.cancel(NotificationId.SHORTCUT_ACTION)
        }

        super.onDestroy()
    }

    private suspend fun toggleVPN() {
        mutex.withLock {
            try {
                val server = getDefaultServer()

                if (tunnelManager.tunnels.value.any { tunnel -> tunnel.server == server || tunnel.tunnelName == server?.id }) {
                    stopVPN()
                } else {
                    startVPN(server)
                }
            } catch (e: Exception) {
                LogPile.error(
                    message = "Error toggling VPN connection via Quick Connect Service.",
                    cause = e
                )

                launchMainActivity()
            }
        }
    }

    private suspend fun startVPN(server: Server?) {
        if (server != null) {
            if (tunnelManager.prepare(context = this)) {
                tunnelManager.connect(server)
            } else {
                launchMainActivity()
            }
        } else {
            launchMainActivity()
        }
    }

    private suspend fun stopVPN() {
        tunnelManager.disconnectAll()

        val vpnIntent = Intent(this, MooncloakVpnService::class.java)
        stopService(vpnIntent)
    }

    private fun launchMainActivity() {
        val mainIntent = MainActivity.newIntent(this).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        launchActivity(mainIntent)
    }

    public companion object {

        private fun newIntent(
            context: Context,
        ): Intent = Intent(context, QuickConnectService::class.java)

        public fun launchFrom(context: Context) {
            val intent = newIntent(context = context)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }
}
