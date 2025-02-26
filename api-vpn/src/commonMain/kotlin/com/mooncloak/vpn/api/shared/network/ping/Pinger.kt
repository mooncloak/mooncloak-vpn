package com.mooncloak.vpn.api.shared.network.ping

import kotlin.time.Duration

/**
 * Interface for performing a ping operation to a specified host.
 *
 * This interface defines a contract for classes that can measure the latency (round-trip time) to a network host.
 * Implementations of this interface should provide a way to send a ping request to a given host and return the
 * measured duration or null if the ping was unsuccessful.
 */
public interface Pinger {

    /**
     * The protocol used for the ping operation.
     *
     * This property specifies the network protocol (e.g., ICMP, TCP, UDP) that will be used when sending ping packets.
     * It determines how the ping operation is performed and what kind of responses are expected.
     */
    public val protocol: PingProtocol

    /**
     * Attempts to ping the specified host and returns the round-trip time (RTT) as a [Duration], or `null` if the ping
     * could not be performed.
     *
     * @param [host] The hostname or IP address to ping.
     *
     * @return The round-trip time (RTT) as a [Duration], or `null` if the ping fails.
     */
    public suspend fun ping(host: String): Duration?

    public companion object
}
