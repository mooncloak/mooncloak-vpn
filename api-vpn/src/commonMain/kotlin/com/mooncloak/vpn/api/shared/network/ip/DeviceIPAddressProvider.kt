package com.mooncloak.vpn.api.shared.network.ip

/**
 * A component that retrieves the Internet Protocol (IP) Address of the current device running this application.
 */
public interface DeviceIPAddressProvider {

    /**
     * Retrieves the public facing IP Address of the current device, or `null` if it could not be obtained.
     */
    public suspend fun get(): String?

    /**
     * Invalidates any underlying cache for this [DeviceIPAddressProvider].
     */
    public suspend fun invalidate()

    public companion object
}
