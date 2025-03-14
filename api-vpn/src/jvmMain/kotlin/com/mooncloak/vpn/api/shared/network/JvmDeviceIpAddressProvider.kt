package com.mooncloak.vpn.api.shared.network

import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.warning
import com.mooncloak.vpn.api.shared.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.api.shared.network.ip.PublicDeviceIPAddressProvider
import com.mooncloak.vpn.data.shared.cache.Cache
import com.mooncloak.vpn.data.shared.keyvalue.get
import com.mooncloak.vpn.data.shared.keyvalue.set

public operator fun PublicDeviceIPAddressProvider.Companion.invoke(
    mooncloakApi: MooncloakVpnServiceHttpApi,
    cache: Cache
): PublicDeviceIPAddressProvider = JvmDeviceIpAddressProvider(
    mooncloakApi = mooncloakApi,
    cache = cache
)

internal class JvmDeviceIpAddressProvider internal constructor(
    private val mooncloakApi: MooncloakVpnServiceHttpApi,
    private val cache: Cache
) : PublicDeviceIPAddressProvider {

    override suspend fun get(): String? {
        cache.get<String>(key = CACHE_KEY)?.let { return it }

        return getFresh()
    }

    override suspend fun invalidate() {
        cache.remove(key = CACHE_KEY)
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
