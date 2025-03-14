package com.mooncloak.vpn.network.core.ping

import kotlinx.serialization.Serializable

/**
 * Represents the protocol used when pinging a server to determine latency.
 */
@Serializable
public enum class PingProtocol(
    public val serialName: String
) {

    /**
     * Internet Control Message Protocol (ICMP).
     */
    @kotlinx.serialization.SerialName(value = SerialName.ICMP)
    ICMP(serialName = SerialName.ICMP),

    /**
     * User Datagram Protocol (UDP).
     */
    @kotlinx.serialization.SerialName(value = SerialName.UDP)
    UDP(serialName = SerialName.UDP),

    /**
     * Transmission control protocol (TCP).
     */
    @kotlinx.serialization.SerialName(value = SerialName.TCP)
    TCP(serialName = SerialName.TCP),

    /**
     * Hypertext Transfer Protocol (HTTP).
     */
    @kotlinx.serialization.SerialName(value = SerialName.HTTP)
    HTTP(serialName = SerialName.HTTP);

    public object SerialName {

        public const val ICMP: String = "icmp"
        public const val UDP: String = "udp"
        public const val TCP: String = "tcp"
        public const val HTTP: String = "http"
    }

    public companion object {

        public operator fun get(serialName: String): PingProtocol? =
            entries.firstOrNull { protocol -> protocol.serialName.equals(serialName, ignoreCase = true) }
    }
}

/**
 * Retrieves the platform-specific default [PingProtocol].
 */
public expect val PingProtocol.Companion.PlatformDefault: PingProtocol
