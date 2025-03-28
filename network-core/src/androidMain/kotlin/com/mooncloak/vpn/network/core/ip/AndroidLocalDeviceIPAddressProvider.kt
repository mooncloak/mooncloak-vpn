package com.mooncloak.vpn.network.core.ip

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import androidx.annotation.RequiresPermission
import java.net.InetAddress

public operator fun LocalDeviceIPAddressProvider.Companion.invoke(context: Context): LocalDeviceIPAddressProvider =
    AndroidLocalDeviceIPAddressProvider(context = context)

internal class AndroidLocalDeviceIPAddressProvider internal constructor(
    private val context: Context
) : LocalDeviceIPAddressProvider {

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun get(): String? = getDeviceIpAddress(context)

    override suspend fun invalidate() {
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
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

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
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
