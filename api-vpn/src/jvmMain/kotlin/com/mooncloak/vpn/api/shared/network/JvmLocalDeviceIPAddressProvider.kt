package com.mooncloak.vpn.api.shared.network

import com.mooncloak.vpn.api.shared.network.ip.LocalDeviceIPAddressProvider

public operator fun LocalDeviceIPAddressProvider.Companion.invoke(): LocalDeviceIPAddressProvider =
    JvmLocalDeviceIPAddressProvider()

internal class JvmLocalDeviceIPAddressProvider internal constructor() : LocalDeviceIPAddressProvider {

    override suspend fun get(): String? = null

    override suspend fun invalidate() {
    }
}
