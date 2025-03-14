package com.mooncloak.vpn.network.core.ip

public operator fun LocalDeviceIPAddressProvider.Companion.invoke(): LocalDeviceIPAddressProvider =
    JvmLocalDeviceIPAddressProvider()

internal class JvmLocalDeviceIPAddressProvider internal constructor() : LocalDeviceIPAddressProvider {

    override suspend fun get(): String? = null

    override suspend fun invalidate() {
    }
}
