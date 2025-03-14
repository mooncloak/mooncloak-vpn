package com.mooncloak.vpn.network.core.ip

import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.warning
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.data.shared.cache.Cache
import com.mooncloak.vpn.data.shared.keyvalue.get
import com.mooncloak.vpn.data.shared.keyvalue.set

internal open class DefaultPublicDeviceIPAddressProvider internal constructor(
    private val mooncloakApi: VpnServiceApi,
    private val cache: Cache
) : PublicDeviceIPAddressProvider {

    final override suspend fun get(): String? {
        cache.get<String>(key = CACHE_KEY)?.let { return it }

        return getFresh()
    }

    final override suspend fun invalidate() {
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
