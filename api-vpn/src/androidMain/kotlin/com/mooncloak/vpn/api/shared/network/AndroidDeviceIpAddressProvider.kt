package com.mooncloak.vpn.api.shared.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.warning
import com.mooncloak.vpn.api.shared.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.data.shared.cache.Cache
import com.mooncloak.vpn.data.shared.keyvalue.get
import com.mooncloak.vpn.data.shared.keyvalue.set
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

public operator fun DeviceIPAddressProvider.Companion.invoke(
    context: Context,
    mooncloakApi: MooncloakVpnServiceHttpApi,
    cache: Cache,
    coroutineScope: CoroutineScope
): DeviceIPAddressProvider = AndroidDeviceIpAddressProvider(
    context = context,
    mooncloakApi = mooncloakApi,
    cache = cache,
    coroutineScope = coroutineScope
)

@SuppressLint("MissingPermission")
internal class AndroidDeviceIpAddressProvider internal constructor(
    context: Context,
    private val mooncloakApi: MooncloakVpnServiceHttpApi,
    private val cache: Cache,
    private val coroutineScope: CoroutineScope
) : DeviceIPAddressProvider {

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

    override suspend fun get(): String? {
        cache.get<String>(key = CACHE_KEY)?.let { return it }

        return getFresh()
    }

    override suspend fun invalidate() {
        cache.remove(key = CACHE_KEY)
    }

    private fun launchInvalidate() {
        coroutineScope.launch {
            invalidate()
        }
    }

    private suspend fun getFresh(): String? {
        val result = runCatching { mooncloakApi.getReflection().ipAddress }
        val ipAddress = result.getOrNull()

        if (result.isFailure) {
            LogPile.warning(
                message = "Error retrieving public IP address.",
                cause = result.exceptionOrNull()
            )
        }

        cache.set<String>(key = CACHE_KEY, value = ipAddress)

        return ipAddress
    }

    internal companion object {

        private const val CACHE_KEY: String = "JvmDeviceIpAddressProviderKey"
    }
}
