package com.mooncloak.vpn.api.shared.network

public operator fun LocalNetworkManager.Companion.invoke(): LocalNetworkManager = JvmLocalNetworkManager()

internal class JvmLocalNetworkManager internal constructor() : LocalNetworkManager {

    // TODO: Implement JVMLocalNetworkManager
    override suspend fun getInfo(): LocalNetworkInfo? = null
}
