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
        LogPile.info(tag = TAG, message = "onReceive: action: ${intent.action}")

        if (intent.action == ACTION) {
            LogPile.info(tag = TAG, message = "Received broadcast to disconnect tunnels.")

            val application = (context.applicationContext as? MooncloakVpnApplication)
            val tunnelManager = application?.applicationComponent?.tunnelManager

            if (tunnelManager != null) {
                LogPile.info(tag = TAG, message = "Attempting to disconnect from all tunnels.")

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
