package com.mooncloak.vpn.api.shared.network.ip

/**
 * A [DeviceIPAddressProvider] that retrieves the public network IP address of the device running this application. For
 * instance, if the device is on a WiFi router, then this would typically provide the router's IP address that is
 * visible to servers when making HTTP requests. However, this would not provide the local IP address on the device
 * within the local network (LAN), for that you would use a [LocalDeviceIPAddressProvider].
 */
public interface PublicDeviceIPAddressProvider : DeviceIPAddressProvider {

    public companion object
}
