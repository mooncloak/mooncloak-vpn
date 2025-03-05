package com.mooncloak.vpn.api.shared.network.ip

/**
 * A [DeviceIPAddressProvider] that retrieves the local network IP address of the device running this application.
 */
public interface LocalDeviceIPAddressProvider : DeviceIPAddressProvider {

    public companion object
}
