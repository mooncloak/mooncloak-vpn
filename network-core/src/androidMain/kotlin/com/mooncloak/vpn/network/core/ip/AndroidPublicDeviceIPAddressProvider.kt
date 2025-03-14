package com.mooncloak.vpn.network.core.ip

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.data.shared.cache.Cache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

public operator fun PublicDeviceIPAddressProvider.Companion.invoke(
    context: Context,
    mooncloakApi: VpnServiceApi,
    cache: Cache,
    coroutineScope: CoroutineScope
): PublicDeviceIPAddressProvider = AndroidPublicDeviceIPAddressProvider(
    context = context,
    mooncloakApi = mooncloakApi,
    cache = cache,
    coroutineScope = coroutineScope
)

@SuppressLint("MissingPermission")
internal class AndroidPublicDeviceIPAddressProvider internal constructor(
    context: Context,
    mooncloakApi: VpnServiceApi,
    cache: Cache,
    private val coroutineScope: CoroutineScope
) : DefaultPublicDeviceIPAddressProvider(
    mooncloakApi = mooncloakApi,
    cache = cache
) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            launchInvalidate()
        }

        override fun onUnavailable() {
            launchInvalidate()
        }

        override fun onLost(network: Network) {
            launchInvalidate()
        }
    }

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun launchInvalidate() {
        coroutineScope.launch {
            invalidate()
        }
    }
}
