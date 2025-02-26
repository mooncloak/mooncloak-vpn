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
import kotlinx.datetime.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public operator fun DeviceIPAddressProvider.Companion.invoke(
    context: Context,
    mooncloakApi: MooncloakVpnServiceHttpApi,
    clock: Clock = Clock.System,
    cachePeriod: Duration = 30.seconds
): DeviceIPAddressProvider = AndroidDeviceIpAddressProvider(
    context = context,
    mooncloakApi = mooncloakApi,
    clock = clock,
    cachePeriod = cachePeriod
)

@SuppressLint("MissingPermission")
internal class AndroidDeviceIpAddressProvider internal constructor(
    context: Context,
    private val mooncloakApi: MooncloakVpnServiceHttpApi,
    private val clock: Clock,
    private val cachePeriod: Duration
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

    private var cachedAt = clock.now()
    private var cachedIpAddress: String? = null

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override suspend fun get(): String? {
        val ipAddress = cachedIpAddress

        val expiration = cachedAt + cachePeriod

        if (ipAddress != null && expiration < clock.now()) {
            return ipAddress
        }

        return getFresh()
    }

    override fun invalidate() {
        TODO("Not yet implemented")
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

        cachedAt = clock.now()
        cachedIpAddress = ipAddress

        return ipAddress
    }
}
