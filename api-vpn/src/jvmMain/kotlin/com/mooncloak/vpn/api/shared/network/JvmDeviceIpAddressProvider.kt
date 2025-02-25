package com.mooncloak.vpn.api.shared.network

import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.warning
import com.mooncloak.vpn.api.shared.MooncloakVpnServiceHttpApi
import kotlinx.datetime.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public operator fun DeviceIPAddressProvider.Companion.invoke(
    mooncloakApi: MooncloakVpnServiceHttpApi,
    clock: Clock = Clock.System,
    cachePeriod: Duration = 30.seconds
): DeviceIPAddressProvider = JvmDeviceIpAddressProvider(
    mooncloakApi = mooncloakApi,
    clock = clock,
    cachePeriod = cachePeriod
)

internal class JvmDeviceIpAddressProvider internal constructor(
    private val mooncloakApi: MooncloakVpnServiceHttpApi,
    private val clock: Clock,
    private val cachePeriod: Duration
) : DeviceIPAddressProvider {

    private var cachedAt = clock.now()
    private var cachedIpAddress: String? = null

    override suspend fun get(): String? {
        val ipAddress = cachedIpAddress

        val expiration = cachedAt + cachePeriod

        if (ipAddress != null && expiration < clock.now()) {
            return ipAddress
        }

        return getFresh()
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
