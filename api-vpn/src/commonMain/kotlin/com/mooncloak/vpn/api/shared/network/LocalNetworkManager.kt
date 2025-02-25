package com.mooncloak.vpn.api.shared.network

public interface LocalNetworkManager {

    public suspend fun getInfo(): LocalNetworkInfo?

    public companion object
}
