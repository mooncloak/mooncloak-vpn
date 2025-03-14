package com.mooncloak.vpn.network.core.ip

/**
 * A [DeviceIPAddressProvider] that retrieves the local network (LAN) IP address of the device running this application.
 */
public interface LocalDeviceIPAddressProvider : DeviceIPAddressProvider {

    public companion object
}
