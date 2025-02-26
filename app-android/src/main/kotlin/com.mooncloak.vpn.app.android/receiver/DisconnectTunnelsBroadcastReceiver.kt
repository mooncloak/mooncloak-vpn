package com.mooncloak.vpn.app.android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.info
import com.mooncloak.vpn.app.android.MooncloakVpnApplication
import kotlinx.coroutines.launch

internal class DisconnectTunnelsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION) {
            val application = (context.applicationContext as? MooncloakVpnApplication)
            val tunnelManager = application?.applicationComponent?.tunnelManager

            if (tunnelManager != null) {
                application.coroutineScope.launch {
                    tunnelManager.disconnectAll()
                }
            } else {
                LogPile.info(tag = TAG, message = "Failed to obtain TunnelManager. Cannot disconnect tunnels.")
            }
        }
    }

    internal companion object {

        private const val TAG: String = "DisconnectTunnelsBroadcastReceiver"
        internal const val ACTION: String = "com.mooncloak.vpn.app.android.receiver.DISCONNECT_TUNNELS"
    }
}
