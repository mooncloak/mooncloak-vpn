package com.mooncloak.vpn.api.shared.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import java.net.InetAddress

public operator fun LocalNetworkManager.Companion.invoke(context: Context): LocalNetworkManager =
    AndroidLocalNetworkManager(context = context)

internal class AndroidLocalNetworkManager internal constructor(
    private val context: Context
) : LocalNetworkManager {

    // TODO: Implement AndroidLocalNetworkManager
    override suspend fun getInfo(): LocalNetworkInfo = LocalNetworkInfo(
        ipAddress = getDeviceIpAddress(context)
    )

    private fun getDeviceIpAddress(context: Context): String? {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return null
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return null

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> getWifiIpAddress(context)
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> getCellularIpAddress(
                connectivityManager
            )

            else -> null
        }
    }

    private fun getWifiIpAddress(context: Context): String? {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress

        return intToIp(ipAddress)
    }

    private fun getCellularIpAddress(connectivityManager: ConnectivityManager): String? {
        val networks = connectivityManager.allNetworks
        for (network in networks) {
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            if (capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true) {
                val linkProperties = connectivityManager.getLinkProperties(network)
                for (address in linkProperties?.linkAddresses.orEmpty()) {
                    val inetAddress = address.address
                    if (!inetAddress.isLoopbackAddress && inetAddress is InetAddress) {
                        return inetAddress.hostAddress
                    }
                }
            }
        }
        return null
    }

    private fun intToIp(ipAddress: Int): String? = InetAddress.getByAddress(
        byteArrayOf(
            (ipAddress ushr 24).toByte(),
            (ipAddress ushr 16).toByte(),
            (ipAddress ushr 8).toByte(),
            ipAddress.toByte()
        )
    ).hostAddress
}
