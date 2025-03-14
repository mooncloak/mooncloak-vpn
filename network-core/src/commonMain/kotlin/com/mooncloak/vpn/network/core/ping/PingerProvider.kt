package com.mooncloak.vpn.network.core.ping

/**
 * Provides instances of [Pinger] based on the specified [PingProtocol].
 *
 * This interface acts as a factory for creating different types of [Pinger] implementations. Implementations of this
 * interface are responsible for determining which [Pinger] instance to return for a given protocol.
 */
public fun interface PingerProvider {

    /**
     * Retrieves a [Pinger] instance associated with the specified [PingProtocol], or `null` if the provided [protocol]
     * is unsupported.
     *
     * @param [protocol] The [PingProtocol] for which to retrieve a Pinger.
     *
     * @return A [Pinger] instance if one is registered for the given protocol; otherwise, null.
     */
    public operator fun get(protocol: PingProtocol): Pinger?

    public companion object
}

public expect operator fun PingerProvider.Companion.invoke(): PingerProvider

/**
 * Retrieves the platform-specific default [Pinger] or `null` if there is no default for the current platform.
 */
public inline fun PingerProvider.default(): Pinger? = get(protocol = PingProtocol.PlatformDefault)
