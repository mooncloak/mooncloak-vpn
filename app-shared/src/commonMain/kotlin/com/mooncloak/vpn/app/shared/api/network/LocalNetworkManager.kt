package com.mooncloak.vpn.app.shared.api.network

public interface LocalNetworkManager {

    public suspend fun getInfo(): LocalNetworkInfo?

    public companion object
}
