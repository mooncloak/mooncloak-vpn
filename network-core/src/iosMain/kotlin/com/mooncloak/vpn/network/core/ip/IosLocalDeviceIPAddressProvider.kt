package com.mooncloak.vpn.network.core.ip

public operator fun LocalDeviceIPAddressProvider.Companion.invoke(): LocalDeviceIPAddressProvider =
    IosLocalDeviceIPAddressProvider()

internal class IosLocalDeviceIPAddressProvider internal constructor() : LocalDeviceIPAddressProvider {

    override suspend fun get(): String? = null

    override suspend fun invalidate() {
    }
}
