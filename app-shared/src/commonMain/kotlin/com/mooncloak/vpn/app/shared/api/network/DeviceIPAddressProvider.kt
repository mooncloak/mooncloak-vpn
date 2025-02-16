package com.mooncloak.vpn.app.shared.api.network

/**
 * A component that retrieves the Internet Protocol (IP) Address of the current device running this application.
 */
public fun interface DeviceIPAddressProvider {

    /**
     * Retrieves the public facing IP Address of the current device, or `null` if it could not be obtained.
     */
    public suspend fun get(): String?

    public companion object
}
