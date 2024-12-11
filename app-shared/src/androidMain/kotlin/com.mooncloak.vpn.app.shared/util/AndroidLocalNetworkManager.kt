package com.mooncloak.vpn.app.shared.util

import android.content.Context
import android.net.wifi.WifiManager
import com.mooncloak.kodetools.konstruct.annotations.Inject


internal class AndroidLocalNetworkManager @Inject internal constructor(
    private val context: ApplicationContext
): LocalNetworkManager {

    override fun getIpAddress(): String {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.connectionInfo.ipAddress
        TODO("Not yet implemented")
    }
}
