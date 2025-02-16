package com.mooncloak.vpn.app.shared.api.network

import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes

public operator fun DeviceIPAddressProvider.Companion.invoke(
    mooncloakApi: MooncloakVpnServiceHttpApi,
    clock: Clock = Clock.System
): DeviceIPAddressProvider = JvmDeviceIpAddressProvider(
    mooncloakApi = mooncloakApi,
    clock = clock
)

internal class JvmDeviceIpAddressProvider internal constructor(
    private val mooncloakApi: MooncloakVpnServiceHttpApi,
    private val clock: Clock
) : DeviceIPAddressProvider {

    private var cachedAt = clock.now()
    private var cachedIpAddress: String? = null

    override suspend fun get(): String? {
        val ipAddress = cachedIpAddress

        val expiration = cachedAt + 5.minutes

        if (ipAddress != null && expiration < clock.now()) {
            return ipAddress
        }

        return getFresh()
    }

    private suspend fun getFresh(): String? {
        val ipAddress = mooncloakApi.getReflection().ipAddress

        cachedAt = clock.now()
        cachedIpAddress = ipAddress

        return ipAddress
    }
}
