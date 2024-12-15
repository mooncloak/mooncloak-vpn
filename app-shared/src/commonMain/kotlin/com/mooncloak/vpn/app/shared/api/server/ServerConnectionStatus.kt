package com.mooncloak.vpn.app.shared.api.server

import androidx.compose.runtime.Immutable

/**
 * Represents the status of the connection between the device and the VPN server.
 */
@Immutable
public enum class ServerConnectionStatus {

    /**
     * The device is not connected to any VPN server.
     */
    Disconnected,

    /**
     * The device is attempting to connect to a VPN server.
     */
    Connecting,

    /**
     * The device is connected to a VPN server.
     */
    Connected,

    /**
     * The connection of the device to a VPN server is being checked. This represents an unknown state.
     */
    Checking
}
