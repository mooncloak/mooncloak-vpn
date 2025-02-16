package com.mooncloak.vpn.app.shared.api.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.util.ApplicationContext

public operator fun DeviceIPAddressProvider.Companion.invoke(
    context: ApplicationContext,
    mooncloakApi: MooncloakVpnServiceHttpApi
): DeviceIPAddressProvider = AndroidDeviceIpAddressProvider(
    context = context,
    mooncloakApi = mooncloakApi
)

@SuppressLint("MissingPermission")
internal class AndroidDeviceIpAddressProvider internal constructor(
    context: ApplicationContext,
    private val mooncloakApi: MooncloakVpnServiceHttpApi
) : DeviceIPAddressProvider {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            cachedIpAddress = null
        }

        override fun onUnavailable() {
            cachedIpAddress = null
        }

        override fun onLost(network: Network) {
            cachedIpAddress = null
        }
    }

    private var cachedIpAddress: String? = null

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override suspend fun get(): String? {
        cachedIpAddress?.let { return it }

        return getFresh()
    }

    private suspend fun getFresh(): String? {
        val ipAddress = mooncloakApi.getReflection().ipAddress

        cachedIpAddress = ipAddress

        return ipAddress
    }
}
